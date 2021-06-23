package dev.soha.course202002.schedule.web.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service class TokenStorageService {
	companion object {
		val TAG = TokenStorageService::class.qualifiedName
		val PREFIX_COOKIE = "$TAG:cookie"
		val PREFIX_USERNAME = "$TAG:username"
	}
	@Autowired private lateinit var operations: ReactiveRedisOperations<String, String>

	suspend fun newSession(cookie: String) = UUID.randomUUID().toString().also {
		operations.opsForValue().setAndAwait("$PREFIX_COOKIE:$it", cookie, Duration.ofMinutes(10))
	}

	suspend fun getSession(token: String) = operations.opsForValue().getAndAwait("$PREFIX_COOKIE:$token")
	suspend fun getUsername(token: String) = operations.opsForValue().getAndAwait("$PREFIX_USERNAME:$token")

	suspend fun refreshSession(token: String, username: String) = operations.opsForValue().run {
		setAndAwait("$PREFIX_COOKIE:$token", getAndAwait("$PREFIX_COOKIE:$token")!!, Duration.ofMinutes(45))
		setAndAwait("$PREFIX_USERNAME:$token", username, Duration.ofMinutes(45))
	}
}