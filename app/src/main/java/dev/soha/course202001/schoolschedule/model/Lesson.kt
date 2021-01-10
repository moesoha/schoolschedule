package dev.soha.course202001.schoolschedule.model

import androidx.room.*
import kotlinx.serialization.Serializable
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
)
