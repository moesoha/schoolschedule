package dev.soha.course202001.schoolschedule.ui.today

import android.app.Application
import androidx.lifecycle.*
import dev.soha.course202001.schoolschedule.helper.DateHelper
import dev.soha.course202001.schoolschedule.model.Lesson
import dev.soha.course202001.schoolschedule.repository.LessonRepository
import dev.soha.course202001.schoolschedule.repository.SettingRepository
import kotlinx.coroutines.launch
import java.util.*

class TodayViewModel(application: Application): AndroidViewModel(application) {
	private val settingRepository = SettingRepository(application)
	private val repository = LessonRepository(application)
	val lessons = MediatorLiveData<List<Lesson>>()
	val currentWeekNumber = liveData { emit(settingRepository.getCurrentRelativeWeekNumber()) }

	init {
		viewModelScope.launch {
			lessons.addSource(repository.getLessonsByDayAndWeek(
				DateHelper.convertCalendarDowToDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)),
				settingRepository.getCurrentRelativeWeekNumber()
			)) {
				lessons.postValue(it)
			}
		}
	}
}