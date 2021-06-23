package dev.soha.course202002.schedule.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScheduleFetcherApplication

fun main(args: Array<String>) {
	runApplication<ScheduleFetcherApplication>(*args)
}