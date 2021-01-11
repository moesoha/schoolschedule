package dev.soha.course202001.schoolschedule.ui.lesson

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.soha.course202001.schoolschedule.model.Lesson
import dev.soha.course202001.schoolschedule.repository.LessonRepository

class LessonViewModel(application: Application, lessonId: Long): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)
	val lesson: LiveData<Lesson> = lessonRepository.find(lessonId)
}