package dev.soha.course202002.schedule.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@EnableRedisHttpSession
@Configuration class HttpSessionConfig {
	@Bean fun connectionFactory(): LettuceConnectionFactory? = LettuceConnectionFactory()
}