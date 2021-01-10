package dev.soha.course202001.schoolschedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LessonWeek(
	val start: Int,
	val end: Int,
	val type: Int
)
