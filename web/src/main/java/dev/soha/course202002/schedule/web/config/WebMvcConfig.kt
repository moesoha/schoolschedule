package dev.soha.course202002.schedule.web.config

import dev.soha.course202002.schedule.web.interceptor.WebSessionInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration class WebMvcConfig : WebMvcConfigurer {
	@Bean fun getWebSessionInterceptor(): WebSessionInterceptor? = WebSessionInterceptor()

	override fun addInterceptors(registry: InterceptorRegistry) {
		super.addInterceptors(registry)
		registry.addInterceptor(getWebSessionInterceptor()!!)
	}
}