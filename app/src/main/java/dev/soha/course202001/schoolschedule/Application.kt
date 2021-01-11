package dev.soha.course202001.schoolschedule

import android.app.Application as AndroidApplication

class Application: AndroidApplication() {
	companion object {
		const val APPLICATION_ID = BuildConfig.APPLICATION_ID

		const val OA_URL_PREFIX = "https://oa.ojc.lohu.info"
		const val OA_URL_LOGIN_START = "$OA_URL_PREFIX/user/oa/start"
		const val OA_URL_LOGIN_SUBMIT = "$OA_URL_PREFIX/user/oa/login/%s"
		const val OA_URL_SCHEDULE_CURRENT = "$OA_URL_PREFIX/schedule/oa/get/%s/current"
	}
}