package dev.soha.course202002.schedule.web.data

import dev.soha.course202002.schedule.model.data.LessonWeek
import dev.soha.course202002.schedule.model.data.Lesson as DataLesson

data class Lesson(
	var id: Int?,
	var username: String?,
	var name: String,
	var teacher: String,
	var place: String,
	var day: Int,
	var session: String?,
	var weekStart: Int,
	var weekEnd: Int,
	var weekType: Int
) {
	var sessionList: List<Int>
		get() = session.orEmpty().split(",").map { it.toInt() }
		set(value) { session = value.joinToString(",") }

	fun toDataObject() = DataLesson(
		name,
		teacher,
		place,
		day,
		sessionList,
		LessonWeek(weekStart, weekEnd, weekType)
	)

	companion object {
		fun fromDataObject(lesson: DataLesson, username: String) = lesson.run {
			Lesson(
				null,
				username,
				name,
				teacher,
				place,
				day,
				"",
				week.start,
				week.end,
				week.type
			).apply { sessionList = this@run.session }
		}
	}
}
