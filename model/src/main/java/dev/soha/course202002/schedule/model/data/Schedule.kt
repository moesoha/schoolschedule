package dev.soha.course202002.schedule.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
	val year: String,
	val semester: String,
	val lessons: List<Lesson>
)
