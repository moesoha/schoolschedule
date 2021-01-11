package dev.soha.course202001.schoolschedule.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import dev.soha.course202001.schoolschedule.Application
import dev.soha.course202001.schoolschedule.VolleyRequestQueue
import dev.soha.course202001.schoolschedule.data.AppDatabase
import dev.soha.course202001.schoolschedule.data.LessonDao
import dev.soha.course202001.schoolschedule.data.SettingDao
import dev.soha.course202001.schoolschedule.model.response.GeneralResponse
import dev.soha.course202001.schoolschedule.model.response.Lesson
import dev.soha.course202001.schoolschedule.model.response.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.coroutines.suspendCoroutine

class LessonRepository(context: Context) {
	companion object {
		val TAG = LessonRepository::class.qualifiedName
	}

	val settingDao: SettingDao = AppDatabase.getDatabase(context).settingDao()
	val lessonDao: LessonDao = AppDatabase.getDatabase(context).lessonDao()
	val volley: VolleyRequestQueue = VolleyRequestQueue.getInstance(context)

	fun getAllLessons() = lessonDao.getAllLessons()

	suspend fun syncWithOa(token: String) {
		val lessons = suspendCoroutine<List<Lesson>> { continuation ->
			Log.d(TAG, "syncWithOa started")
			volley.add(JsonObjectRequest(
				Request.Method.GET,
				Application.OA_URL_SCHEDULE_CURRENT.format(token),
				null,
				{ response ->
					val response = Json.decodeFromString<GeneralResponse<Schedule>>(response.toString())
					Log.d(TAG, "Response: %s".format(response))
					continuation.resumeWith(Result.success(response.data.lessons))
				},
				{ error ->
					Log.e(TAG, "Error: %s".format(error))
					continuation.resumeWith(Result.failure(error))
				}
			))
		}
		lessonDao.removeAllImportedLessons()
		lessons.forEach {
			lessonDao.insert(it.toLesson())
		}
	}
}