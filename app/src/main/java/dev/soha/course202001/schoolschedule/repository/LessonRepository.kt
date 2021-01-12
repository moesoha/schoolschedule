package dev.soha.course202001.schoolschedule.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import dev.soha.course202001.schoolschedule.Application
import dev.soha.course202001.schoolschedule.R
import dev.soha.course202001.schoolschedule.VolleyRequestQueue
import dev.soha.course202001.schoolschedule.data.AppDatabase
import dev.soha.course202001.schoolschedule.data.LessonDao
import dev.soha.course202001.schoolschedule.model.response.GeneralResponse
import dev.soha.course202001.schoolschedule.model.response.Lesson
import dev.soha.course202001.schoolschedule.model.response.Schedule
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.time.DayOfWeek
import kotlin.coroutines.suspendCoroutine

class LessonRepository(context: Context) {
	companion object {
		val TAG = LessonRepository::class.qualifiedName
	}

	val lessonDao: LessonDao = AppDatabase.getDatabase(context).lessonDao()
	val volley: VolleyRequestQueue = VolleyRequestQueue.getInstance(context)

	fun getAllLessons() = lessonDao.getAllLessons()
	fun getLessonsByDay(dow: DayOfWeek) = lessonDao.getLessonsByDay(dow)

	fun find(id: Long) = lessonDao.find(id)

	suspend fun syncWithOa(token: String) {
		val lessons = suspendCoroutine<List<Lesson>> { continuation ->
			Log.d(TAG, "syncWithOa started")
			volley.add(JsonObjectRequest(
				Request.Method.POST,
				Application.OA_URL_SCHEDULE_CURRENT.format(token),
				null,
				{ json ->
					val response = Json.decodeFromString<GeneralResponse<Schedule>>(json.toString())
					Log.d(TAG, "Response: %s".format(response))
					continuation.resumeWith(Result.success(response.data.lessons))
				},
				{ error ->
					Log.e(TAG, "Error: %s".format(error))
					continuation.resumeWith(Result.failure(error))
				}
			))
		}
		val colors = Application.res.getIntArray(R.array.lesson_candidate_color)
		val lessonMap = HashMap<String, Int>()
		val usedColorMap = HashSet<Int>()
		lessonDao.removeAllImportedLessons()
		lessons.forEach {
			val lesson = it.toLesson()

			if (usedColorMap.size == colors.size) {
				usedColorMap.clear()
			}
			val colorKey = "${lesson.name}|${lesson.teacher}"
			val color = lessonMap.get(colorKey) ?: colors.first { !usedColorMap.contains(it) }
			lessonMap[colorKey] = color
			usedColorMap.add(color)
			lesson.color = color

			lessonDao.insert(lesson)
		}
	}
}