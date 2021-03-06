package dev.soha.course202002.schedule.web.interceptor

import dev.soha.course202002.schedule.web.annotation.AllowAnonymous
import dev.soha.course202002.schedule.web.service.SessionFetcherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class WebSessionInterceptor : HandlerInterceptor {
	@Autowired private lateinit var applicationContext: ApplicationContext

	private fun isWebController(handler: Any): Boolean {
		if (handler is HandlerMethod) {
			return handler.beanType.`package`.name === "dev.soha.course202002.schedule.web.controller.web"
		}
		return false
	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		if (isWebController(handler)) {
			val handler = handler as HandlerMethod
			if (!applicationContext.getBean(SessionFetcherService::class.java).loggedIn) {
				if (
					!handler.hasMethodAnnotation(AllowAnonymous::class.java) &&
					!handler.beanType.isAnnotationPresent(AllowAnonymous::class.java)
				) {
					response.sendRedirect("/web/auth/login")
					return false
				}
			}
		}
		return super.preHandle(request, response, handler)
	}

	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
		if (isWebController(handler)) {
			applicationContext.getBean(SessionFetcherService::class.java).saveToHttpSession()
		}
	}
}