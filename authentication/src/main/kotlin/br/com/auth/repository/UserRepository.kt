package br.com.auth.repository

import br.com.auth.entities.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {

    @Query(value = "SELECT u FROM User u WHERE u.email =:email")
    fun fetchByEmail(
        @Param("email") email: String?
    ): User?

    fun findByEmail(
        email: String?
    ): UserDetails?

    @Query(value = "SELECT u FROM User u WHERE u.id =:id AND u.email =:email")
    fun fetchUserLoggedByIdAndEmail(
        @Param("id") userId: Long? = null,
        @Param("email") email: String
    ): User?

    @Query(value = "SELECT s FROM User s WHERE s.userName = :userName")
    fun checkUsernameAlreadyExisting(
        @Param("userName") userName: String
    ): User?

    @Modifying
    @Query(value = "UPDATE User u SET u.name =:name, u.surname =:surname, u.userName =:userName WHERE u.id =:id")
    fun changeInfoUserLogged(
        @Param("id") userId: Long? = null,
        @Param("name") name: String,
        @Param("surname") surname: String,
        @Param("userName") userName: String
    )

    @Query(value = "SELECT s FROM User s WHERE s.email = :email")
    fun checkEmailAlreadyExisting(
        @Param("email") email: String
    ): User?

    @Modifying
    @Query(value = "UPDATE User u SET u.email =:email WHERE u.id =:id")
    fun changeEmailUserLogged(
        @Param("id") userId: Long? = null,
        @Param("email") email: String
    )

    @Modifying
    @Query(value = "UPDATE User u SET u.password =:password WHERE u.id =:id")
    fun changePasswordUserLogged(
        @Param("id") userId: Long? = null,
        @Param("password") password: String
    )
}