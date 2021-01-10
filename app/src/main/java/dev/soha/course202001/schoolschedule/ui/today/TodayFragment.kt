package dev.soha.course202001.schoolschedule.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.soha.course202001.schoolschedule.R

class TodayFragment: Fragment() {

	private lateinit var todayViewModel: TodayViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)
		val root = inflater.inflate(R.layout.fragment_today, container, false)
		return root
	}
}