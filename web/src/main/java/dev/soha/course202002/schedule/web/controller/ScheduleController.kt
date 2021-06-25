package dev.soha.course202002.schedule.web.controller

import dev.soha.course202002.schedule.model.data.Schedule
import dev.soha.course202002.schedule.model.response.Response
import dev.soha.course202002.schedule.web.OaFetcher
import dev.soha.course202002.schedule.web.data.LessonMapper
import dev.soha.course202002.schedule.web.service.TokenStorageService
import org.springframework.web.bind.annotation.*

@RequestMapping("/schedule")
@RestController class ScheduleController(
	private val tokenStore: TokenStorageService,
	private val lessonMapper: LessonMapper
) {
	@OptIn(ExperimentalStdlibApi::class)
	@GetMapping("/oa/get/{token}/current")
	suspend fun oaGetCurrentAction(@PathVariable token: String): Response<Schedule> {
		val session = tokenStore.getSession(token)
		val username = tokenStore.getUsername(token)
		if (session.isNullOrBlank() || username.isNullOrBlank()) {
			return Response(404, "Not found!", null)
		}
		val request = OaFetcher().apply { cookieSessionId = session }
		val schedule = request.fetchSchedule(username)
		tokenStore.refreshSession(token, username)
		val localLessons = lessonMapper.findByUsername(username)
		return Response.new(Schedule(
			schedule.year,
			schedule.semester,
			buildList { addAll(schedule.lessons); addAll(localLessons.map { it.toDataObject() }); }
		))
	}
}