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
	private val _loginCredentials = MutableLiveData<Pair<String?, String?>>(Pair(null, null))
	val loginCredentials: LiveData<Pair<String?, String?>> = _loginCredentials

	private var token: String? = null

	fun loadCredentials() = viewModelScope.launch(Dispatchers.IO) {
		val cred = loginRepository.getCredential()
		withContext(Dispatchers.Main) {
			_loginCredentials.value = cred
		}
	}

	fun loginStart() {
		_captchaBitmap.value = null
		var token: String? = null
		viewModelScope.launch(Dispatchers.IO) {
			var captcha = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
			try {
				val data = loginRepository.loginStart()
				captcha = data.second
				token = data.first
				Log.d(TAG, "loginStart Success")
			} catch (e: Exception) {
				Log.e(TAG, "loginStart Exception: %s".format(e))
			}
			withContext(Dispatchers.Main) {
				_captchaBitmap.value = captcha
				this@OaLoginViewModel.token = token
			}
		}
	}

	fun login(username: String, password: String, captcha: String) {
		_submitting.value = true
		val token = this.token
		viewModelScope.launch(Dispatchers.IO) {
			var success = false
			try {
				success = loginRepository.loginSubmit(token!!, username, password, captcha)
				Log.d(TAG, "login Success: %s".format(success))
			} catch(e: Exception) {
				Log.e(TAG, "login Exception: %s".format(e))
			}
			withContext(Dispatchers.Main) {
				_loginSuccess.value = success
				_submitting.value = false
			}
		}
	}
}