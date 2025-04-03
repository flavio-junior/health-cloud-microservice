package br.com.health.cloud.vo

import br.com.health.cloud.utils.common.Gender
import com.fasterxml.jackson.annotation.JsonProperty

data class PatientResponseVO(
    var id: Long = 0,
    @JsonProperty(value = "created_at")
    var createdAt: String? = "",
    var name: String? = null,
    var surname: String? = null,
    @JsonProperty(value = "full_name")
    var fullName: String? = null,
    var gender: Gender? = null,
    var address: AddressResponseVO? = null
)
