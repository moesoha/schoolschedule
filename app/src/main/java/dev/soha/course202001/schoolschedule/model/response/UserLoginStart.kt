package dev.soha.course202001.schoolschedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginStart(
	val token: String,
	val captcha: String
)
