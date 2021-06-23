package dev.soha.course202002.schedule.web.controller

import dev.soha.course202002.schedule.model.request.UserLoginSubmit as UserLoginSubmitRequest
import dev.soha.course202002.schedule.model.response.Response
import dev.soha.course202002.schedule.model.response.UserLoginStart
import dev.soha.course202002.schedule.model.response.UserLoginSubmit as UserLoginSubmitResponse
import dev.soha.course202002.schedule.web.OaFetcher
import dev.soha.course202002.schedule.web.service.TokenStorageService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val tokenStore: TokenStorageService) {
	@GetMapping("/oa/start")
	suspend fun oaStartAction(): Response<UserLoginStart> {
		val request = OaFetcher()
		val captcha = request.getCaptcha()
		return Response.new(UserLoginStart(tokenStore.newSession(request.cookieSessionId!!), captcha))
	}

	@PostMapping("/oa/login/{token}")
	suspend fun oaLoginAction(
		@PathVariable token: String,
		@RequestBody data: UserLoginSubmitRequest
	): Response<UserLoginSubmitResponse> {
		val session = tokenStore.getSession(token)
		if (session.isNullOrBlank()) {
			return Response(404, "Not found!", null)
		}
		val request = OaFetcher().apply { cookieSessionId = session }
		val success = request.login(data.username, data.password, data.captcha)
		if (success) {
			tokenStore.refreshSession(token, data.username)
		}
		return Response.new(UserLoginSubmitResponse(success))
	}
}