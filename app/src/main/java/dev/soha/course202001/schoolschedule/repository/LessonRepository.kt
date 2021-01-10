package dev.soha.course202001.schoolschedule.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import dev.soha.course202001.schoolschedule.VolleyRequestQueue
import dev.soha.course202001.schoolschedule.data.AppDatabase
import dev.soha.course202001.schoolschedule.data.LessonDao
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LessonRepository(context: Context) {
	companion object {
		val TAG = LessonRepository::class.qualifiedName
	}

	val lessonDao: LessonDao = AppDatabase.getDatabase(context).lessonDao()
	val volley: VolleyRequestQueue = VolleyRequestQueue.getInstance(context)

	suspend fun syncWithOa() = suspendCoroutine<Unit> { continuation ->
		Log.d(TAG, "syncWithOa started")
		volley.add(JsonObjectRequest(
			Request.Method.GET,
			"https://oa.ojc.lohu.info/schedule/oa/get/demo/current",
			null,
			{ response ->
				Log.d(TAG, "Response: %s".format(response))
				continuation.resume(Unit)
			},
			{ error -> continuation.resume(Unit) }
		))
	}
}