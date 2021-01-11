package dev.soha.course202001.schoolschedule.ui.lesson

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class LessonViewModelFactory(private val application: Application, private val id: Long): ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T = LessonViewModel(application, id) as T
}