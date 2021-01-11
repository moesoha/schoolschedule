package dev.soha.course202001.schoolschedule.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginSubmit(
	var username: String,
	var password: String,
	var captcha: String,
)
