package dev.soha.course202002.schedule.web.service

import dev.soha.course202002.schedule.model.data.Schedule
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
		const val SESSION_KEY_FETCHER_SCHEDULE = "FetcherSchedule"
	}

	@Autowired private lateinit var session: HttpSession
	private lateinit var fetcher: OaFetcher

	private var _username: String? = null
	val username: String?
		get() = _username
	val loggedIn: Boolean
		get() = !_username.isNullOrBlank()

	private var _schedule: Schedule? = null
	val schedule: Schedule?
		get() = _schedule

	override fun afterPropertiesSet() {
		fetcher = OaFetcher()
		loadFromHttpSession()
	}

	fun loadFromHttpSession() {
		(session.getAttribute(SESSION_KEY_FETCHER_SESSION_ID) as String?)?.let { fetcher.cookieSessionId = it }
		(session.getAttribute(SESSION_KEY_FETCHER_USERNAME) as String?)?.let { _username = it }
		(session.getAttribute(SESSION_KEY_FETCHER_SCHEDULE) as Schedule?)?.let { _schedule = it }
	}
	fun saveToHttpSession() {
		session.setAttribute(SESSION_KEY_FETCHER_SESSION_ID, fetcher.cookieSessionId)
		session.setAttribute(SESSION_KEY_FETCHER_USERNAME, _username)
		session.setAttribute(SESSION_KEY_FETCHER_SCHEDULE, _schedule)
	}

	suspend fun getCaptcha() = fetcher.getCaptcha()
	suspend fun login(username: String, password: String, captcha: String) = fetcher.login(username, password, captcha).also {
		if (it) this._username = username
	}
	suspend fun fetchSchedule() = _schedule ?: _username?.let { fetcher.fetchSchedule(it).also { _schedule = it } }
}