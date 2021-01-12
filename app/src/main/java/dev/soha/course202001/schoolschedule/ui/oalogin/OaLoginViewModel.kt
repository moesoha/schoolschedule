package dev.soha.course202001.schoolschedule.ui.oalogin

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import dev.soha.course202001.schoolschedule.repository.OaLoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OaLoginViewModel(application: Application): AndroidViewModel(application) {
	companion object {
		val TAG = OaLoginViewModel::class.qualifiedName
	}

	private val loginRepository: OaLoginRepository = OaLoginRepository(application)

	private val _captchaBitmap = MutableLiveData<Bitmap?>(null)
	val captchaBitmap: LiveData<Bitmap?> = _captchaBitmap
	private val _submitting = MutableLiveData(false)
	val submitting: LiveData<Boolean> = _submitting
	private val _loginSuccess = MutableLiveData<Boolean?>(null)
	val loginSuccess: LiveData<Boolean?> = _loginSuccess

	val loginCredentials: LiveData<Pair<String?, String?>> = liveData {
		emit(loginRepository.getCredential())
	}

	private var token: String? = null

	fun loginStart() {
		_captchaBitmap.value = null
		viewModelScope.launch {
			var captcha = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
			try {
				val data = loginRepository.loginStart()
				captcha = data.second
				token = data.first
				Log.d(TAG, "loginStart Success")
			} catch (e: Exception) {
				Log.e(TAG, "loginStart Exception: %s".format(e))
			}
			_captchaBitmap.value = captcha
		}
	}

	fun login(username: String, password: String, captcha: String) {
		_submitting.value = true
		val token = this.token
		viewModelScope.launch {
			var success = false
			try {
				success = loginRepository.loginSubmit(token!!, username, password, captcha)
				Log.d(TAG, "login Success: %s".format(success))
			} catch(e: Exception) {
				Log.e(TAG, "login Exception: %s".format(e))
			}
			_loginSuccess.value = success
			_submitting.value = false
		}
	}
}