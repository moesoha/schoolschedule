package dev.soha.course202002.schedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginStart(
	val token: String,
	val captcha: String
)
