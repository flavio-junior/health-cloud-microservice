package br.com.health.cloud.entities

import br.com.health.cloud.utils.common.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tb_patient")
data class Patient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null,
    var name: String? = null,
    var surname: String? = null,
    @Column(name = "full_name", unique = true)
    var fullName: String? = null,
    var gender: Gender? = null,
    @OneToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.EAGER)
    var address: Address? = null
)
