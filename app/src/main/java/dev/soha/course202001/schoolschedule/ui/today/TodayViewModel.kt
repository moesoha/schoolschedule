package dev.soha.course202001.schoolschedule.ui.today

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.soha.course202001.schoolschedule.repository.LessonRepository

class TodayViewModel(application: Application): AndroidViewModel(application) {
	private val repository = LessonRepository(application)
	val lessons = this.repository.getAllLessons()
}