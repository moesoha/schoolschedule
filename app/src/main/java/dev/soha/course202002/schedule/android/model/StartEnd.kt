package dev.soha.course202002.schedule.android.model

data class StartEnd(var start: Int, var end: Int) {
	val span: Int
		get() = (end - start) + 1
}
