package dev.soha.course202001.schoolschedule.model

import android.content.res.Resources
import android.graphics.Color
import androidx.room.*
import dev.soha.course202001.schoolschedule.Application
import dev.soha.course202001.schoolschedule.R
import java.time.DayOfWeek

/** 一节课的信息 */
@Entity(tableName = "lesson")
data class Lesson(
	@PrimaryKey(autoGenerate=true)
	@ColumnInfo(name="_id")
	val id: Long = 0,

	/** 课程名称 */
	@ColumnInfo(name="name")
	var name: String,

	/** 教师名称 */
	@ColumnInfo(name="teacher")
	var teacher: String,

	/** 上课地点 */
	@ColumnInfo(name="place")
	var place: String,

	/** 周几上课 */
	@ColumnInfo(name="day")
	var day: DayOfWeek,

	/** 节次起讫 */
	@Embedded(prefix = "session_")
	var session: StartEnd,

	/** 周次起讫 */
	@Embedded(prefix = "week_")
	var week: StartEnd,

	/** 周循环 */
	@ColumnInfo(name="week_repetition")
	var weekRepetition: Int,

	@ColumnInfo(name="color")
	var color: Int? = null
) {
	private fun getString(resId: Int, vararg args: Any) = Application.res.getString(resId, *args)
	private fun getStringArray(resId: Int) = Application.res.getStringArray(resId)

	val sessionString: String
		get() = getString(R.string.lesson_session_formatted, session.start, session.end)
	val weekString: String
		get() = getString(R.string.lesson_week_formatted, week.start, week.end)
	val weekDayString: String
		get() = when (weekRepetition) {
			0 -> "???"
			1 -> getString(R.string.lesson_time_week_formatted_1, getStringArray(R.array.dow)[day.value])
			2 -> getString(
				R.string.lesson_time_week_formatted,
				getStringArray(R.array.week_repetition_2)[week.start % 2],
				getStringArray(R.array.dow)[day.value]
			)
			else -> getString(
				R.string.lesson_time_week_formatted,
				getString(R.string.week_repetition_more, weekRepetition),
				getStringArray(R.array.dow)[day.value]
			)
		}

	fun onWeek(n: Int) = n in (week.start..week.end step weekRepetition)
}
