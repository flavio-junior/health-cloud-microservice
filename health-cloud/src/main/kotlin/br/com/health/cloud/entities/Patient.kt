package br.com.health.cloud.entities

import br.com.health.cloud.utils.common.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tb_patient")
data class Patient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String? = null,
    var surname: String? = null,
    var fullName: String? = null,
    var gender: Gender? = null,
    @OneToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.EAGER)
    var address: Address? = null
)
