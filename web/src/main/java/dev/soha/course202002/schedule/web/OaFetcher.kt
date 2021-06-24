package dev.soha.course202002.schedule.web

import dev.soha.course202002.schedule.model.data.Lesson
import dev.soha.course202002.schedule.model.data.LessonWeek
import dev.soha.course202002.schedule.model.data.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.jetty.client.HttpClient
import org.jsoup.Jsoup
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.JettyClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.BodyInserters.*
import org.springframework.web.reactive.function.client.awaitExchange
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import java.util.*

class OaFetcher {
	companion object {
		private const val SESSION_COOKIE_NAME = "ASP.NET_SessionId"
		private const val BASE_URL = "http://ojjx.wzu.edu.cn/"
		private const val USER_AGENT = "oujiang-college-schedule/1.0@kt-2021 Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36"
		private val WEEKDAYS = arrayOf("日", "一", "二", "三", "四", "五", "六")
		private val REGEXP_TIME = Regex("""^周(.)第([\d,]+)节\{第(\d+)-(\d+)周(\|.周)?\}$""")
	}

	private val httpClient = HttpClient().apply {
		userAgentField = null
		cookieStore = CookieManager().cookieStore
		isFollowRedirects = false
	}
	private val webClient = WebClient.builder()
		.clientConnector(JettyClientHttpConnector(httpClient))
		.baseUrl(BASE_URL)
		.defaultHeader(HttpHeaders.REFERER, BASE_URL)
		.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
		.build()

	var cookieSessionId: String?
		get() = httpClient.cookieStore[URI(BASE_URL)].findLast { it.name == SESSION_COOKIE_NAME }?.value
		set(value) {
			if (value.isNullOrBlank()) with(httpClient.cookieStore[URI(BASE_URL)]) {
				findLast { it.name == SESSION_COOKIE_NAME }?.let { remove(it) }
			} else {
				httpClient.cookieStore.add(URI(BASE_URL), HttpCookie(SESSION_COOKIE_NAME, value))
			}
		}

	suspend fun getCaptcha() = withContext(Dispatchers.IO) { webClient
		.get()
		.uri("CheckCode.aspx")
		.awaitExchange { Pair(
			it.headers().contentType().orElse(MediaType.APPLICATION_OCTET_STREAM),
			it.awaitBody<ByteArrayResource>().byteArray
		) }
	}
	suspend fun getCaptchaDataUri() = getCaptcha().let { (type, data) -> "data:$type;base64,${Base64.getEncoder().encodeToString(data)}" }

	suspend fun login(username: String, password: String, captcha: String): Boolean = withContext(Dispatchers.IO) {
		val formHtml = webClient
			.get()
			.uri("default2.aspx")
			.retrieve()
			.awaitBody<String>()
		val form = Jsoup.parse(formHtml).select("form#form1")
		val viewstate = form.select("input[name='__VIEWSTATE']").`val`()
		return@withContext webClient
			.post()
			.uri("default2.aspx")
			.body(
				fromFormData("Button1", "")
					.with("__VIEWSTATE", viewstate)
					.with("txtUserName", username)
					.with("TextBox2", password)
					.with("txtSecretCode", captcha)

			)
			.awaitExchange { it.rawStatusCode() == 302 }
	}

	suspend fun fetchSchedule(username: String): Schedule {
		val html = withContext(Dispatchers.IO) { webClient
			.get()
			.uri("xskbcx.aspx?xh={xh}&gnmkdm=N121603", username)
			.retrieve()
			.awaitBody<String>()
		}
		val dom = Jsoup.parse(html)
		val year = dom.selectFirst("#xnd option[selected]").attr("value")
		val semester = dom.selectFirst("#xqd option[selected]").attr("value")
		val table = dom.selectFirst("#Table1")
		operator fun<T> List<T>.component6(): T = get(5)
		val lessons = table.select("td")
			.asSequence()
			.filter { it.attr("align").trim().lowercase() == "center" && it.text().isNotBlank() }
			.map { it.html().split(Regex("(<br[^>]*>){2}")).filter { it.isNotBlank() } }
			.flatten()
			.map { it.split(Regex("(<br[^>]*>){1}")).filter { it.isNotBlank() }.map { it.trim() } }
			.filter { it.count() == 4 }
			.map { (name, time, teacher, place) -> REGEXP_TIME.find(time)?.groupValues?.let {
					(_, weekDay, sessions, weekStart, weekEnd, weekType) -> Lesson(
						name,
						teacher,
						place,
						WEEKDAYS.indexOf(weekDay.trim()),
						sessions.split(',').map { it.toInt() },
						LessonWeek(
							weekStart.toInt(),
							weekEnd.toInt(),
							(if (weekType.isBlank()) 0 else if (weekType == "双周") 2 else 1)
						)
					)
				}
			}
			.filterNotNull()
			.toList()
		return Schedule(year, semester, lessons)
	}
}