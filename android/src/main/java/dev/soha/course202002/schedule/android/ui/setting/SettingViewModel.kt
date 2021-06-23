package dev.soha.course202002.schedule.android.ui.setting

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import dev.soha.course202002.schedule.android.R
import dev.soha.course202002.schedule.android.repository.LessonRepository
import dev.soha.course202002.schedule.android.repository.OaLoginRepository
import dev.soha.course202002.schedule.android.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(application: Application): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)
	private val oaLoginRepository = OaLoginRepository(application)
	private val settingRepository = SettingRepository(application)

	private val _loading = MutableLiveData(false)
	val loading: LiveData<Boolean> = _loading

	private val _loggedInUsername = MutableLiveData<String?>(null)
	val loggedInUsername: LiveData<String?> = _loggedInUsername

	private val _currentWeekNumber = MutableLiveData<Int>(1)
	val currentWeekNumber: LiveData<Int> = _currentWeekNumber

	fun loadLoggedInUsername() = viewModelScope.launch {
		val token = oaLoginRepository.getToken()
		_loggedInUsername.value = token?.second
	}

	fun loadSettings() = viewModelScope.launch {
		_currentWeekNumber.value = settingRepository.getCurrentRelativeWeekNumber()
	}

	fun setCurrentWeekNumber(n: Int) = viewModelScope.launch {
		settingRepository.setCurrentRelativeWeekNumber(n)
		_currentWeekNumber.value = n
	}

	fun syncWithOa() = viewModelScope.launch {
		_loading.value = true
		try {
			lessonRepository.syncWithOa(oaLoginRepository.getToken()!!.first)
			withContext(Dispatchers.Main) {
				Toast.makeText(getApplication(), R.string.oa_sync_success, Toast.LENGTH_LONG).show()
			}
		} catch(_: Exception) {
			withContext(Dispatchers.Main) {
				Toast.makeText(getApplication(), R.string.oa_sync_fail, Toast.LENGTH_LONG).show()
			}
		}
		_loading.value = false
	}
}