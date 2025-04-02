package br.com.auth.vo.user

import br.com.auth.utils.TypeAccount
import com.fasterxml.jackson.annotation.JsonProperty

data class SignUpRequestVO(
    val email: String,
    val password: String,
    @JsonProperty(value = "type_account")
    val typeAccount: TypeAccount? = null
)
