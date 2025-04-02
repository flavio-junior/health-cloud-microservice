package br.com.health.cloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HealthCloudApplication

fun main(args: Array<String>) {
	runApplication<HealthCloudApplication>(*args)
}
