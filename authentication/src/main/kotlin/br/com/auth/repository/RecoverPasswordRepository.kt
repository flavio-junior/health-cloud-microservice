package br.com.auth.repository

import br.com.auth.entities.security.RecoverPassword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface RecoverPasswordRepository : JpaRepository<RecoverPassword, Long> {

    @Query("SELECT recover FROM RecoverPassword recover WHERE recover.email =:email")
    fun findRecoverPasswordByEmail(
        @Param("email") email: String?
    ): RecoverPassword?

    @Query(value = "SELECT r FROM RecoverPassword r WHERE r.code = :code")
    fun checkCodeAlreadyExists(
        @Param("code") code: String
    ): RecoverPassword?

    @Modifying
    @Query("UPDATE RecoverPassword r SET r.code =:token, r.expiration =:expiration WHERE r.email =:email")
    fun updateTokenAndDataExpiration(
        @Param("email") email: String?,
        @Param("token") token: Long?,
        @Param("expiration") expiration: LocalDateTime? = null
    )
}
