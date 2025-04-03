package br.com.health.cloud.vo

data class AddressRequestVO(
    var street: String? = "",
    var number: Int? = null,
    var district: String? = "",
    var complement: String? = ""
)
