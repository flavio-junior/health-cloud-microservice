package br.com.health.cloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class HealthCloudApplication

fun main(args: Array<String>) {
	runApplication<HealthCloudApplication>(*args)
}
