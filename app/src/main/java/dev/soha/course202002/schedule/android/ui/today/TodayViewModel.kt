package dev.soha.course202002.schedule.android.ui.today

import android.app.Application
import androidx.lifecycle.*
import dev.soha.course202002.schedule.android.helper.DateHelper
import dev.soha.course202002.schedule.android.model.Lesson
import dev.soha.course202002.schedule.android.repository.LessonRepository
import dev.soha.course202002.schedule.android.repository.SettingRepository
import java.util.*

class TodayViewModel(application: Application): AndroidViewModel(application) {
	private val settingRepository = SettingRepository(application)
	private val repository = LessonRepository(application)
	val lessons: LiveData<List<Lesson>> = repository.getLessonsByDay(DateHelper.convertCalendarDowToDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))
	val currentWeekNumber = liveData { emit(settingRepository.getCurrentRelativeWeekNumber()) }
}