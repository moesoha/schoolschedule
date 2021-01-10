package dev.soha.course202001.schoolschedule.ui.setting

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import dev.soha.course202001.schoolschedule.R
import dev.soha.course202001.schoolschedule.repository.LessonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(application: Application): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)

	fun syncWithOa(callback: (() -> Unit)? = null) = viewModelScope.launch(Dispatchers.IO) {
		try {
			lessonRepository.syncWithOa()
			withContext(Dispatchers.Main) {
				Toast.makeText(getApplication(), R.string.oa_sync_success, Toast.LENGTH_LONG).show()
			}
		} catch(_: Exception) {
			withContext(Dispatchers.Main) {
				Toast.makeText(getApplication(), R.string.oa_sync_fail, Toast.LENGTH_LONG).show()
			}
		} finally {
			withContext(Dispatchers.Main) { callback?.invoke() }
		}
	}
}