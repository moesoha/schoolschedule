package dev.soha.course202001.schoolschedule.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import dev.soha.course202001.schoolschedule.VolleyRequestQueue
import dev.soha.course202001.schoolschedule.data.AppDatabase
import dev.soha.course202001.schoolschedule.data.LessonDao
import dev.soha.course202001.schoolschedule.model.response.GeneralResponse
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

	val lessonDao: LessonDao = AppDatabase.getDatabase(context).lessonDao()
	val volley: VolleyRequestQueue = VolleyRequestQueue.getInstance(context)

	fun getAllLessons() = lessonDao.getAllLessons()

	suspend fun syncWithOa() = suspendCoroutine<Unit> { continuation ->
		Log.d(TAG, "syncWithOa started")
		volley.add(JsonObjectRequest(
			Request.Method.GET,
			"https://oa.ojc.lohu.info/schedule/oa/get/demo/current",
			null,
			{ response ->
				val response = Json.decodeFromString<GeneralResponse<Schedule>>(response.toString())
				Log.d(TAG, "Response: %s".format(response))
				runBlocking(Dispatchers.IO) {
					response.data.lessons.forEach {
						lessonDao.insert(it.toLesson())
					}
				}
				continuation.resumeWith(Result.success(Unit))
			},
			{ error ->
				Log.e(TAG, "Error: %s".format(error))
				continuation.resumeWith(Result.failure(error))
			}
		))
	}
}