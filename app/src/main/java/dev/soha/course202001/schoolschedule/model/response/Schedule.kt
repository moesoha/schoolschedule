package dev.soha.course202001.schoolschedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
	val year: String,
	val semester: String,
	val lessons: List<Lesson>
)
