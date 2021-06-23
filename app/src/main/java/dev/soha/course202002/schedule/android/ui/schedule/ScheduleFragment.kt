package dev.soha.course202002.schedule.android.ui.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.GridLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.soha.course202002.schedule.android.R
import dev.soha.course202002.schedule.android.helper.DateHelper
import dev.soha.course202002.schedule.android.ui.MainActivity
import dev.soha.course202002.schedule.android.ui.lesson.LessonShowActivity
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment: Fragment() {
	companion object {
		val TAG = ScheduleFragment::class.qualifiedName
	}

	private lateinit var scheduleViewModel: ScheduleViewModel
	private var displayingWeekDiff = 0
		set(value) {
			field = value
			updateScheduleLayout()
		}
	private val displayingWeek
		get() = (scheduleViewModel.currentWeekNumber.value ?: 1) + displayingWeekDiff

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java).apply {
			currentWeekNumber.observe(viewLifecycleOwner) {
				Log.v("$TAG,currentWeekNumber", "changed")
				updateScheduleLayout()
			}
			lessons.observe(viewLifecycleOwner) {
				Log.v("$TAG,lessons", "changed")
				updateScheduleLayout()
			}
		}
		val root = inflater.inflate(R.layout.fragment_schedule, container, false)
		setHasOptionsMenu(true)
		return root
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.schedule_menu, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		Log.v("$TAG::onOptionsItemSelected", "invoked")
		return when (item.itemId) {
			R.id.previous_week -> {
				displayingWeekDiff--
				true
			}
			R.id.next_week -> {
				displayingWeekDiff++
				true
			}
			else -> false
		}
	}

	@SuppressLint("SimpleDateFormat", "SetTextI18n")
	private fun updateScheduleLayout() {
		Log.v("$TAG::updateScheduleLayout", "checking")
		val grid: GridLayout = activity?.findViewById(R.id.grid) ?: return
		val lessons = scheduleViewModel.lessons.value ?: return
		(activity as MainActivity).supportActionBar?.title = getString(R.string.week_n, displayingWeek)
		val currentWeekMonday = DateHelper.addWeekFromDate(DateHelper.getMondayOfWeekFromDate(Date()), displayingWeekDiff)

		Log.v("$TAG::updateScheduleLayout", "removing previous views")
		grid.removeAllViews()
		grid.addView(Space(context).apply { layoutParams = GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(0)) })

		Log.v("$TAG::updateScheduleLayout", "started")
		// 显示周几
		val dow = resources.getStringArray(R.array.dow)
		for (i in 1..7) {
			grid.addView(TextView(context).apply {
				layoutParams = GridLayout.LayoutParams(
					GridLayout.spec(0, 1),
					GridLayout.spec(i, 1, 1f)
				).apply {
					width = 0
					height = ViewGroup.LayoutParams.WRAP_CONTENT
				}
				textAlignment = View.TEXT_ALIGNMENT_CENTER
				setTextAppearance(android.R.style.TextAppearance_Small)
				val date = SimpleDateFormat("MM/dd").format(DateHelper.addDayFromDate(currentWeekMonday, i - 1))
				text = "${dow[i]}\n$date"
			})
		}

		// 显示节次
		for (i in 1..12) {
			grid.addView(TextView(context).apply {
				layoutParams = GridLayout.LayoutParams(
					GridLayout.spec(i, 1, 1f),
					GridLayout.spec(0, 1)
				).apply {
					height = this@ScheduleFragment.resources.getDimension(R.dimen.schedule_session_height).toInt()
					gravity = Gravity.FILL_HORIZONTAL
				}
				text = i.toString()
			})
		}

		// 显示课程
		for (lesson in lessons) {
			if (!lesson.onWeek(displayingWeek)) {
				continue
			}
			grid.addView(Button(context).apply {
				layoutParams = GridLayout.LayoutParams(
					GridLayout.spec(lesson.session.start, lesson.session.span, 1f),
					GridLayout.spec(lesson.day.value, 1, 1f),
				).apply {
					height = 0
					width = 0
					setMargins(this@ScheduleFragment.resources.getDimension(R.dimen.schedule_lesson_button_margin).toInt())
				}
				setPadding(
					this@ScheduleFragment.resources.getDimension(R.dimen.schedule_lesson_button_padding).toInt(),
					this@ScheduleFragment.resources.getDimension(R.dimen.schedule_lesson_button_padding_top).toInt(),
					this@ScheduleFragment.resources.getDimension(R.dimen.schedule_lesson_button_padding).toInt(),
					this@ScheduleFragment.resources.getDimension(R.dimen.schedule_lesson_button_padding).toInt()
				)
				isAllCaps = false
				gravity = Gravity.TOP or Gravity.CENTER
				text = "${lesson.name}\n${lesson.place}"
				textSize = resources.getDimension(R.dimen.schedule_lesson_button_text)

				setTextColor(Color.WHITE)
				setBackgroundColor(lesson.color ?: Color.BLACK)
				setOnClickListener {
					startActivity(
						Intent(activity, LessonShowActivity::class.java)
							.putExtra(LessonShowActivity.INTENT_EXTRA_ID, lesson.id)
					)
				}
			})
		}

		Log.v("$TAG::updateScheduleLayout", "finished")
	}
}