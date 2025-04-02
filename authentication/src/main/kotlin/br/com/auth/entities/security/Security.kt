package br.com.auth.entities.security

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tb_security")
data class Security(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,
    @Column(nullable = false)
    var code: Long? = 0,
    @Column(nullable = false, unique = true)
    var email: String? = "",
    @Column(name = "expiration_date")
    var expirationDate: LocalDateTime? = null
)
