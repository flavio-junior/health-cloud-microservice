package br.com.health.cloud.vo

import br.com.health.cloud.utils.common.Country
import br.com.health.cloud.utils.common.UF

data class AddressResponseVO(
    var id: Long = 0,
    var street: String? = "",
    var number: Int? = null,
    var district: String? = "",
    var complement: String? = "",
    var code: Int? = 0,
    var city: String? = "",
    var uf: UF? = null,
    var country: Country? = null
)
