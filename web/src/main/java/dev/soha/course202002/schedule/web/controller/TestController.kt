package dev.soha.course202002.schedule.web.controller

import dev.soha.course202002.schedule.model.response.Response
import dev.soha.course202002.schedule.model.response.UserLoginStart
import dev.soha.course202002.schedule.web.OaFetcher
import dev.soha.course202002.schedule.web.service.TokenStorageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class TestController(private val tokenStore: TokenStorageService) {
	@GetMapping("/hello")
	fun hello(@RequestParam(value = "name", defaultValue = "World") name: String) = String.format("Hello %s!", name)

	@GetMapping("/test")
	suspend fun test(response: HttpServletResponse): Response<UserLoginStart> {
		val request = OaFetcher()
		val captcha = request.getCaptcha()
		val token = tokenStore.newSession(request.cookieSessionId!!)
		return Response.new(UserLoginStart(token, captcha))
	}
}