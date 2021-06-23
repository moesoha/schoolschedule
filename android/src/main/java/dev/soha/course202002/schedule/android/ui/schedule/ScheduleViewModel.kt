package dev.soha.course202002.schedule.android.ui.schedule

import android.app.Application
import androidx.lifecycle.*
import dev.soha.course202002.schedule.android.model.Lesson
import dev.soha.course202002.schedule.android.repository.LessonRepository
import dev.soha.course202002.schedule.android.repository.SettingRepository

class ScheduleViewModel(application: Application): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)
	private val settingRepository = SettingRepository(application)
	val lessons: LiveData<List<Lesson>> = lessonRepository.getAllLessons()

	val currentWeekNumber: LiveData<Int> = liveData {
		emit(settingRepository.getCurrentRelativeWeekNumber())
	}
}