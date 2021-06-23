package dev.soha.course202002.schedule.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
	val name: String,
	val teacher: String,
	val place: String,
	val day: Int,
	val session: List<Int>,
	val week: LessonWeek
)
