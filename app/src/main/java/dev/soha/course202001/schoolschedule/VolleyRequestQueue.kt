package dev.soha.course202001.schoolschedule

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyRequestQueue constructor(context: Context) {
	companion object {
		val TAG = VolleyRequestQueue::class.qualifiedName

		@Volatile private var INSTANCE: VolleyRequestQueue? = null
		fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
			INSTANCE ?: VolleyRequestQueue(context).also { INSTANCE = it }
		}
	}

	val requestQueue: RequestQueue by lazy {
		Volley.newRequestQueue(context.applicationContext)
	}

	fun<T> add(req: Request<T>): Request<T>? {
		Log.v(TAG, "one request added to Volley queue: ${req.method} ${req.url}")
		return requestQueue.add(req)
	}
}