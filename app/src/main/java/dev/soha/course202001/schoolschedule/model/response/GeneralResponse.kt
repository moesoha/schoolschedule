package dev.soha.course202001.schoolschedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GeneralResponse<T>(
	val status: Int,
	val `return`: String,
	val data: T
)
