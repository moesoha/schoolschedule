package dev.soha.course202001.schoolschedule.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import dev.soha.course202001.schoolschedule.Application
import dev.soha.course202001.schoolschedule.VolleyRequestQueue
import dev.soha.course202001.schoolschedule.data.AppDatabase
import dev.soha.course202001.schoolschedule.data.SettingDao
import dev.soha.course202001.schoolschedule.model.Setting
import dev.soha.course202001.schoolschedule.model.request.UserLoginSubmit as UserLoginSubmitRequest
import dev.soha.course202001.schoolschedule.model.response.UserLoginSubmit as UserLoginSubmitResponse
import dev.soha.course202001.schoolschedule.model.response.GeneralResponse
import dev.soha.course202001.schoolschedule.model.response.UserLoginStart
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.suspendCoroutine

class OaLoginRepository(context: Context) {
	companion object {
		val TAG = OaLoginRepository::class.qualifiedName
	}

	val settingDao: SettingDao = AppDatabase.getDatabase(context).settingDao()
	val volley: VolleyRequestQueue = VolleyRequestQueue.getInstance(context)

	suspend fun getToken(): Pair<String, String?>? {
		return try {
			if (Date() >= Date(settingDao.get(Setting.OA_TOKEN_VALID_BEFORE)!!.toLong())) {
				throw Exception()
			}
			Pair(settingDao.get(Setting.OA_TOKEN)!!, settingDao.get(Setting.OA_TOKEN_ASSOCIATED_USERNAME))
		} catch (_: Exception) {
			null
		}
	}
	suspend fun saveToken(token: String, validBefore: Date, associatedUsername: String?) {
		settingDao.set(Setting.OA_TOKEN, token)
		settingDao.set(Setting.OA_TOKEN_VALID_BEFORE, validBefore.time.toString())
		settingDao.set(Setting.OA_TOKEN_ASSOCIATED_USERNAME, associatedUsername)
	}

	suspend fun getCredential(): Pair<String?, String?> = Pair(
		settingDao.get(Setting.OA_USERNAME),
		settingDao.get(Setting.OA_PASSWORD)
	)
	suspend fun saveCredential(username: String?, password: String?) {
		settingDao.set(Setting.OA_USERNAME, username)
		settingDao.set(Setting.OA_PASSWORD, password)
	}

	/**
	 * @return 验证码 base64
	 */
	suspend fun loginStart(): Pair<String, Bitmap> {
		val loginStart = suspendCoroutine<UserLoginStart> { continuation ->
			Log.d(TAG, "loginStart started")
			volley.add(JsonObjectRequest(
				Request.Method.GET,
				Application.OA_URL_LOGIN_START,
				null,
				{ response ->
					val response = Json.decodeFromString<GeneralResponse<UserLoginStart>>(response.toString())
					Log.d(LessonRepository.TAG, "loginStart Response: %s".format(response))
					continuation.resumeWith(Result.success(response.data))
				},
				{ error ->
					Log.e(LessonRepository.TAG, "loginStart Error: %s".format(error))
					continuation.resumeWith(Result.failure(error))
				}
			))
		}
		val (type, data) = """^data:image/(.+?),(.+)$""".toRegex().matchEntire(loginStart.captcha)!!.destructured
		val bytes = Base64.decode(data, Base64.DEFAULT)
		val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

		// saveToken(loginStart.token, Date(Date().time + 8 * 60 * 1000), null)
		return Pair(loginStart.token, bitmap)
	}

	suspend fun loginSubmit(token: String, username: String, password: String, captcha: String): Boolean {
		val loginSuccess = suspendCoroutine<Boolean> { continuation ->
			Log.d(TAG, "loginSubmit started")
			volley.add(JsonObjectRequest(
				Request.Method.POST,
				Application.OA_URL_LOGIN_SUBMIT.format(token),
				JSONObject(Json.encodeToString(UserLoginSubmitRequest(username, password, captcha))),
				{ response ->
					val response = Json.decodeFromString<GeneralResponse<UserLoginSubmitResponse>>(response.toString())
					Log.d(LessonRepository.TAG, "loginSubmit Response: %s".format(response))
					continuation.resumeWith(Result.success(response.data.success))
				},
				{ error ->
					Log.e(LessonRepository.TAG, "loginSubmit Error: %s".format(error))
					continuation.resumeWith(Result.failure(error))
				}
			))
		}
		if (loginSuccess) {
			Log.d(LessonRepository.TAG, "loginSubmit success[$token]: $username")
			saveToken(token, Date(Date().time + 39 * 60 * 1000), username)
			saveCredential(username, password)
		}
		return loginSuccess
	}
}