package dev.soha.course202002.schedule.model.data

import kotlinx.serialization.Serializable

@Serializable
data class LessonWeek(
	val start: Int,
	val end: Int,
	val type: Int
): java.io.Serializable {
	override fun toString(): String {
		return "$start 到 $end 周" + when (type) {
			1 -> "(单周)"
			2 -> "(双周)"
			else -> ""
		}
	}
}
