package dev.soha.course202001.schoolschedule.ui.schedule

import android.app.Application
import androidx.lifecycle.*
import dev.soha.course202001.schoolschedule.model.Lesson
import dev.soha.course202001.schoolschedule.repository.LessonRepository
import dev.soha.course202001.schoolschedule.repository.SettingRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(application: Application): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)
	private val settingRepository = SettingRepository(application)
	val lessons: LiveData<List<Lesson>> = lessonRepository.getAllLessons()

	private val _displayingWeek: MutableLiveData<Int> = MutableLiveData(1)
	val displayingWeek: LiveData<Int> = _displayingWeek

	init {
		viewModelScope.launch {
			_displayingWeek.value = settingRepository.getCurrentRelativeWeekNumber()
		}
	}
}