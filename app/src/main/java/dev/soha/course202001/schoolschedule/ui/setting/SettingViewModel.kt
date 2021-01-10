package dev.soha.course202001.schoolschedule.ui.setting

import android.app.Application
import androidx.lifecycle.*
import dev.soha.course202001.schoolschedule.repository.LessonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)

	fun syncWithOa() = viewModelScope.launch(Dispatchers.IO) { lessonRepository.syncWithOa() }
}