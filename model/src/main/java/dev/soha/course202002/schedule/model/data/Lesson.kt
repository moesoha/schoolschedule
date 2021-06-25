package dev.soha.course202002.schedule.model.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.DayOfWeek

@Serializable
@JsonIgnoreProperties("dayOfWeek")
data class Lesson(
	val name: String,
	val teacher: String,
	val place: String,
	val day: Int,
	val session: List<Int>,
	val week: LessonWeek
): java.io.Serializable {
	@Transient val dayOfWeek: DayOfWeek
		get() = when (day) {
			0 -> DayOfWeek.SUNDAY
			else -> DayOfWeek.of(day)
		}
}
