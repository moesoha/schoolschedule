package dev.soha.course202001.schoolschedule.ui.today

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.soha.course202001.schoolschedule.repository.LessonRepository

class TodayViewModel(application: Application): AndroidViewModel(application) {
	private val repository = LessonRepository(application)
	val lessons = this.repository.getAllLessons()
}