package dev.soha.course202001.schoolschedule.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.soha.course202001.schoolschedule.R

class ScheduleFragment: Fragment() {

	private lateinit var scheduleViewModel: ScheduleViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
		val root = inflater.inflate(R.layout.fragment_schedule, container, false)
//		val textView: TextView = root.findViewById(R.id.text_dashboard)
//		scheduleViewModel.text.observe(viewLifecycleOwner, Observer {
//			textView.text = it
//		})
		return root
	}
}