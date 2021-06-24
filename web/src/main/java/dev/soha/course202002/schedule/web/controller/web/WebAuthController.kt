package dev.soha.course202002.schedule.web.controller.web

import dev.soha.course202002.schedule.model.request.UserLoginSubmit
import dev.soha.course202002.schedule.web.service.SessionFetcherService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletResponse

@RequestMapping("/web/auth")
@Controller class WebAuthController(val fetcherService: SessionFetcherService) {
	@GetMapping("/captcha")
	@ResponseBody
	suspend fun captchaAction(response: HttpServletResponse) {
		val (type, captcha) = fetcherService.getCaptcha()
		response.contentType = type.toString()
		response.outputStream.write(captcha)
	}

	@GetMapping("/login")
	fun loginPage() = ModelAndView("login").apply {
		addObject("form", UserLoginSubmit("", "", ""))
	}
	@PostMapping("/login")
	suspend fun loginAction(@ModelAttribute form: UserLoginSubmit) = ModelAndView("login").apply {
		if (form.username.isNotBlank() && form.password.isNotBlank() && form.captcha.isNotBlank()) {
			if (fetcherService.login(form.username, form.password, form.captcha)) {
				viewName = "redirect:/web"
				modelMap.addAttribute("attribute", "redirectWithRedirectPrefix")
				return@apply
			}
		}
		addObject("failed", true)
		addObject("form", form.apply { password = "" })
	}
}