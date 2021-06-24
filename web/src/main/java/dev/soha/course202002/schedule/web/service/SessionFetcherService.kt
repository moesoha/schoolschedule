package dev.soha.course202002.schedule.web.service

import dev.soha.course202002.schedule.web.OaFetcher
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.context.annotation.RequestScope
import javax.servlet.http.HttpSession

@RequestScope
@Service class SessionFetcherService: InitializingBean {
	companion object {
		const val SESSION_KEY_FETCHER_SESSION_ID = "FetcherSessionId"
		const val SESSION_KEY_FETCHER_USERNAME = "FetcherUsername"
	}

	@Autowired private lateinit var session: HttpSession
	private lateinit var fetcher: OaFetcher
	private var username: String? = null
	val loggedIn: Boolean
		get() = !username.isNullOrBlank()

	override fun afterPropertiesSet() {
		fetcher = OaFetcher()
		loadFromHttpSession()
	}

	fun loadFromHttpSession() {
		(session.getAttribute(SESSION_KEY_FETCHER_SESSION_ID) as String?)?.let { fetcher.cookieSessionId = it }
		(session.getAttribute(SESSION_KEY_FETCHER_USERNAME) as String?)?.let { username = it }
	}
	fun saveToHttpSession() {
		session.setAttribute(SESSION_KEY_FETCHER_SESSION_ID, fetcher.cookieSessionId)
		session.setAttribute(SESSION_KEY_FETCHER_USERNAME, username)
	}

	suspend fun getCaptcha() = fetcher.getCaptcha()
	suspend fun login(username: String, password: String, captcha: String) = fetcher.login(username, password, captcha).also {
		if (it) this.username = username
	}
	suspend fun fetchSchedule() = fetcher.fetchSchedule(username!!)
}