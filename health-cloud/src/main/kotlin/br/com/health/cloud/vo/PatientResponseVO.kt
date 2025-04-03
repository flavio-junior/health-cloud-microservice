package br.com.health.cloud.vo

import br.com.health.cloud.utils.common.Gender
import com.fasterxml.jackson.annotation.JsonProperty

data class PatientResponseVO(
    var id: Long = 0,
    var name: String? = null,
    var surname: String? = null,
    @JsonProperty(value = "full_name")
    var fullName: String? = null,
    var gender: Gender? = null,
    var address: AddressResponseVO? = null
)
