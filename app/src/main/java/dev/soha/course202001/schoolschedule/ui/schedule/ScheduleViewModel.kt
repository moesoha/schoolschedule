package dev.soha.course202001.schoolschedule.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScheduleViewModel: ViewModel() {

	private val _text = MutableLiveData<String>().apply {
		value = "This is dashboard Fragment"
	}
	val text: LiveData<String> = _text
}