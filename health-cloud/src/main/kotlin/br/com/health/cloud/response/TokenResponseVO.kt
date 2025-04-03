package br.com.health.cloud.response

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponseVO(
    val user: String? = null,
    val authenticated: Boolean? = null,
    @JsonProperty(value = "created_at")
    val createdAt: String? = null,
    @JsonProperty(value = "type_account")
    val typeAccount: TypeAccount? = null,
    @JsonProperty(value = "expiration_date")
    val expirationDate: String? = null,
    @JsonProperty(value = "access_token")
    val accessToken: String? = null,
    @JsonProperty(value = "refresh_token")
    val refreshToken: String? = null
)
