package dev.soha.course202002.schedule.android.ui.lesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import dev.soha.course202002.schedule.android.R

class LessonShowActivity: AppCompatActivity() {
	companion object {
		val TAG = LessonShowActivity::class.qualifiedName
		val INTENT_EXTRA_ID = "$TAG:id"
	}

	private lateinit var viewModel: LessonViewModel
	private var lessonId: Long = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_lesson_show)
		if (!intent.hasExtra(INTENT_EXTRA_ID)) {
			finish()
		}
		lessonId = intent.getLongExtra(INTENT_EXTRA_ID, 0)

		val viewLessonName: TextView = findViewById(R.id.lesson_name)
		val viewLessonPlace: TextView = findViewById(R.id.lesson_place)
		val viewLessonWeek: TextView = findViewById(R.id.lesson_week)
		val viewLessonTime: TextView = findViewById(R.id.lesson_time)
		val viewLessonSession: TextView = findViewById(R.id.lesson_session)
		val viewLessonTeacher: TextView = findViewById(R.id.lesson_teacher)

		this.viewModel = ViewModelProvider(this, LessonViewModelFactory(application, lessonId)).get(LessonViewModel::class.java).apply {
			lesson.observe(this@LessonShowActivity) { lesson ->
				viewLessonName.text = lesson.name
				viewLessonPlace.text = lesson.place
				viewLessonTeacher.text = lesson.teacher
				viewLessonSession.text = lesson.sessionString
				viewLessonTime.text = lesson.weekDayString
				viewLessonWeek.text = lesson.weekString
			}
		}
	}
}