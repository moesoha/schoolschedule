package dev.soha.course202001.schoolschedule.model

data class StartEnd(var start: Int, var end: Int) {
	val span: Int
		get() = (end - start) + 1
}
