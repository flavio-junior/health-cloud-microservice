package br.com.auth.vo.user

import com.fasterxml.jackson.annotation.JsonProperty

data class UserAuthenticatedResponseVO(
    var id: Long? = 0,
    @JsonProperty(value = "created_at")
    var createdAt: String? = null,
    var name: String? = null,
    var surname: String? = null,
    @JsonProperty(value = "username")
    var userName: String? = null,
    var email: String? = null,
    @JsonProperty(value = "type_account")
    var type: TypeAccountVO? = null
)
