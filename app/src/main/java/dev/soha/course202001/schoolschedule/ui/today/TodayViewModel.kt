package dev.soha.course202001.schoolschedule.ui.today

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.soha.course202001.schoolschedule.repository.LessonRepository
import java.time.DayOfWeek
import java.util.*

class TodayViewModel(application: Application): AndroidViewModel(application) {
	private val repository = LessonRepository(application)
	private val dayOfWeekValue = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
	val lessons = this.repository.getLessonsByDay(DayOfWeek.of(if (dayOfWeekValue > 0) dayOfWeekValue else 7))
}