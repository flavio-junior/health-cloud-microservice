package br.com.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
