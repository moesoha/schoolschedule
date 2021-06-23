package dev.soha.course202002.schedule.android.ui.today

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.soha.course202002.schedule.android.R
import dev.soha.course202002.schedule.android.adapter.LessonCardListAdapter
import dev.soha.course202002.schedule.android.ui.lesson.LessonShowActivity

class TodayFragment: Fragment() {
	private lateinit var todayViewModel: TodayViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)
		val root = inflater.inflate(R.layout.fragment_today, container, false)

		val lessonAdapter = LessonCardListAdapter(context) { lesson ->
			startActivity(
				Intent(activity, LessonShowActivity::class.java)
					.putExtra(LessonShowActivity.INTENT_EXTRA_ID, lesson.id)
			)
		}
		root.findViewById<RecyclerView>(R.id.lesson_list).apply {
			adapter = lessonAdapter
			layoutManager = LinearLayoutManager(context)
		}
		todayViewModel.lessons.observe(viewLifecycleOwner) {
			lessonAdapter.data = it.filter { todayViewModel.currentWeekNumber.value?.let { w -> it.onWeek(w) } ?: true }
		}

		return root
	}
}