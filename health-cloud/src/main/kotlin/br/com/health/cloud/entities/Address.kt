package br.com.health.cloud.entities

import br.com.health.cloud.utils.common.Country
import br.com.health.cloud.utils.common.UF
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tb_address")
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var street: String? = "",
    var number: Int? = null,
    var district: String? = "",
    @Column(nullable = false, unique = true)
    var complement: String? = "",
    @Column(nullable = false, name = "zip_code")
    var code: Int? = 0,
    @Column(nullable = false)
    var city: String? = "",
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var uf: UF? = null,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var country: Country? = Country.BR
)
