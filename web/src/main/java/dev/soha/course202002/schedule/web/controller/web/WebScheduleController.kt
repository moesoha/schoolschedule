package dev.soha.course202002.schedule.web.controller.web

import dev.soha.course202002.schedule.web.service.SessionFetcherService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RequestMapping("/web")
@Controller class WebScheduleController(val fetcherService: SessionFetcherService) {
	@GetMapping("") suspend fun schedulePage() = ModelAndView("schedule").apply {
		addObject("username", fetcherService.username)
		addObject("schedule", fetcherService.fetchSchedule())
	}
}