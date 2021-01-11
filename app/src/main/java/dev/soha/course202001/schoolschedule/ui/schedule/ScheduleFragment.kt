package dev.soha.course202001.schoolschedule.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.soha.course202001.schoolschedule.R
import dev.soha.course202001.schoolschedule.ui.lesson.LessonShowActivity

class ScheduleFragment: Fragment() {
	companion object {
		val TAG = ScheduleFragment::class.qualifiedName
	}

	private lateinit var scheduleViewModel: ScheduleViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java).apply {
			var isFirstObserved = true
			displayingWeek.observe(viewLifecycleOwner) {
				activity?.actionBar?.title = getString(R.string.week_n, it)
				if (isFirstObserved) {
					isFirstObserved = false
					return@observe
				}
				updateScheduleLayout()
			}
			lessons.observe(viewLifecycleOwner) {
				updateScheduleLayout()
			}
		}
		val root = inflater.inflate(R.layout.fragment_schedule, container, false)
		return root
	}

	fun updateScheduleLayout() {
		Log.v("$TAG::updateScheduleLayout", "checking")
		val grid: GridLayout = activity?.findViewById(R.id.grid) ?: return
		val lessons = scheduleViewModel.lessons.value ?: return

		Log.v("$TAG::updateScheduleLayout", "started")
		val usedSpace = Array(7) { Array(12) { false } }
		for (lesson in lessons) {
			val button = Button(context).apply {
				layoutParams = GridLayout.LayoutParams(
					GridLayout.spec(lesson.session.start, lesson.session.span, 1f),
					GridLayout.spec(lesson.day.value, 1, 1f),
				).apply {
					height = 0
					width = 0
				}
				text = lesson.name
				setOnClickListener {
					startActivity(
						Intent(activity, LessonShowActivity::class.java)
							.putExtra(LessonShowActivity.INTENT_EXTRA_ID, lesson.id)
					)
				}
			}
			grid.addView(button)
			for (i in lesson.session.start..lesson.session.end) {
				usedSpace[lesson.day.value - 1][i - 1] = true
			}
		}

		Log.v("$TAG::updateScheduleLayout", "finished")
	}
}