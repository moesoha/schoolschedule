package dev.soha.course202001.schoolschedule.model.response

import dev.soha.course202001.schoolschedule.model.StartEnd
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import dev.soha.course202001.schoolschedule.model.Lesson as LocalLesson

@Serializable
data class Lesson(
	val name: String,
	val teacher: String,
	val place: String,
	val day: Int,
	val session: List<Int>,
	val week: LessonWeek
) {
	fun getDayOfWeek(): DayOfWeek = when (day) {
		0 -> DayOfWeek.SUNDAY
		else -> DayOfWeek.of(day)
	}

	fun toLesson() = LocalLesson(
		name = name,
		teacher = teacher,
		place = place,
		day = getDayOfWeek(),
		session = StartEnd(session[0], session[1]),
		week = StartEnd(when (week.type) {
			1, 2 -> if ((week.start % 2) != (week.type % 2)) week.start + 1 else week.start
			else -> week.start
		}, week.end),
		weekRepetition = when (week.type) {
			0 -> 1
			else -> 2
		}
	)
}