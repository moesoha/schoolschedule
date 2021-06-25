package dev.soha.course202002.schedule.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginSubmit(
	var username: String,
	var password: String,
	var captcha: String,
): java.io.Serializable