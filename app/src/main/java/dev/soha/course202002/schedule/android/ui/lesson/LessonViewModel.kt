package dev.soha.course202002.schedule.android.ui.lesson

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.soha.course202002.schedule.android.model.Lesson
import dev.soha.course202002.schedule.android.repository.LessonRepository

class LessonViewModel(application: Application, lessonId: Long): AndroidViewModel(application) {
	private val lessonRepository = LessonRepository(application)
	val lesson: LiveData<Lesson> = lessonRepository.find(lessonId)
}