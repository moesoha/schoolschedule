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
	}

	@Autowired private lateinit var session: HttpSession
	lateinit var fetcher: OaFetcher

	override fun afterPropertiesSet() {
		fetcher = OaFetcher()
		loadFromHttpSession()
	}

	fun loadFromHttpSession() = (session.getAttribute(SESSION_KEY_FETCHER_SESSION_ID) as String?)?.let { fetcher.cookieSessionId = it }
	fun saveToHttpSession() = session.setAttribute(SESSION_KEY_FETCHER_SESSION_ID, fetcher.cookieSessionId)
}