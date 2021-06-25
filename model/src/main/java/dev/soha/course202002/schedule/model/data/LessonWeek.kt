package dev.soha.course202002.schedule.model.data

import kotlinx.serialization.Serializable

@Serializable
data class LessonWeek(
	val start: Int,
	val end: Int,
	val type: Int
): java.io.Serializable
