package dev.soha.course202002.schedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
	val status: Int,
	val `return`: String,
	val data: T?
): java.io.Serializable {
	companion object {
		fun<T> new(data: T): Response<T> = Response(200, "OK", data)
	}
}