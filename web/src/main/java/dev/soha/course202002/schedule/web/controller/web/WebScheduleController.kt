package dev.soha.course202002.schedule.web.controller.web

import dev.soha.course202002.schedule.web.data.LessonMapper
import dev.soha.course202002.schedule.web.service.SessionFetcherService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import dev.soha.course202002.schedule.web.data.Lesson as LocalLesson


@RequestMapping("/web")
@Controller class WebScheduleController(
	private val fetcherService: SessionFetcherService,
	private val lessonMapper: LessonMapper
) {
	@GetMapping("")
	suspend fun schedulePage() = ModelAndView("schedule").apply {
		addObject("username", fetcherService.username)
		addObject("schedule", fetcherService.fetchSchedule())
		val lessons = lessonMapper.findByUsername(fetcherService.username!!)
			.map { Pair(it.id, it.toDataObject()) }.toMap()
		addObject("localSchedule", lessons)
	}

	@GetMapping("/lesson/new")
	suspend fun lessonNewPage() = ModelAndView("lessonForm").apply {
		addObject("newObject", true)
		addObject("form", LocalLesson(null, "", "", "", "", 1, "", 1, 1, 0))
	}

	@PostMapping("/lesson/new")
	suspend fun lessonNewAction(@ModelAttribute form: LocalLesson) = RedirectView("/web").also {
		lessonMapper.insert(form.apply { username = fetcherService.username!! })
	}

	@GetMapping("/lesson/edit")
	suspend fun lessonEditPage(@RequestParam id: Int) = ModelAndView("lessonForm").apply {
		val lesson = lessonMapper.findOne(id)
		if (lesson?.username != fetcherService.username!!) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found")
		}
		addObject("form", lesson)
	}

	@PostMapping("/lesson/edit")
	suspend fun lessonEditAction(@RequestParam id: Int, @ModelAttribute form: LocalLesson) = ModelAndView("lessonForm").apply {
		if (lessonMapper.findOne(id)?.username != fetcherService.username!!) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found")
		}
		lessonMapper.update(form.apply {
			this.id = id
			username = fetcherService.username!!
		})
		addObject("form", form)
	}

	@PostMapping("/lesson/delete")
	suspend fun lessonDeleteAction(@RequestParam id: Int) = RedirectView("/web").also {
		val lesson = lessonMapper.findOne(id)
		if (lesson?.username != fetcherService.username!!) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found")
		}
		lessonMapper.delete(lesson)
	}
}