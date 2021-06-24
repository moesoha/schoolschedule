package dev.soha.course202002.schedule.web.controller.web

import dev.soha.course202002.schedule.web.service.SessionFetcherService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse

@RequestMapping("/web/auth")
@Controller class AuthController(val fetcherService: SessionFetcherService) {
	@GetMapping("/captcha")
	@ResponseBody
	suspend fun captchaAction(response: HttpServletResponse) {
		val (type, captcha) = fetcherService.fetcher.getCaptcha()
		response.contentType = type.toString()
		response.outputStream.write(captcha)
	}
}