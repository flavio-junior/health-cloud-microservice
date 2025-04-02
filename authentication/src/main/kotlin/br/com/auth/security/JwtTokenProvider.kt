package br.com.auth.security

import br.com.auth.exceptions.InvalidJwtAuthenticationException
import br.com.auth.utils.TypeAccount
import br.com.auth.utils.getLocalDateTime
import br.com.auth.utils.toDate
import br.com.auth.vo.user.TokenResponseVO
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class JwtTokenProvider {

    @Value("\${security.jwt.token.secret-key:secret}")
    private var secretKey = "secret"

    @Value("\${security.jwt.token.expire-length}")
    private var tokenExpiresAt: Long = 0

    @Autowired
    private lateinit var userDetailsService: UserDetailsService
    private lateinit var algorithm: Algorithm

    @PostConstruct
    internal fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
        algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    }

    fun createAccessToken(
        username: String,
        typeAccount: TypeAccount
    ): TokenResponseVO {
        val currentLocalDateTime = getLocalDateTime()
        val validity = currentLocalDateTime.plusDays(tokenExpiresAt)
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dateCreation = currentLocalDateTime.format(formatter)
        val dateExpirationCreated = validity.format(formatter)
        val accessTokenCreated = getAccessToken(
            username = username,
            typeAccount = typeAccount,
            currentDateTime = currentLocalDateTime,
            validity = validity
        )
        val refreshTokenCreated = getRefreshToken(
            username = username,
            typeAccount = typeAccount,
            currentLocalDateTime = currentLocalDateTime
        )
        return TokenResponseVO(
            user = username,
            authenticated = true,
            typeAccount = typeAccount,
            accessToken = accessTokenCreated,
            refreshToken = refreshTokenCreated,
            createdAt = dateCreation,
            expirationDate = dateExpirationCreated
        )
    }

    fun refreshToken(
        refreshToken: String
    ): TokenResponseVO {
        var token = ""
        if (refreshToken.contains(other = "Bearer ")) token = refreshToken.substring(startIndex = "Bearer ".length)
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        val decodedJWT: DecodedJWT = verifier.verify(token)
        val username: String = decodedJWT.subject
        val typeAccount: TypeAccount = decodedJWT.getClaim("type_account").`as`(TypeAccount::class.java)
        return createAccessToken(username = username, typeAccount = typeAccount)
    }

    fun getAccessToken(
        username: String,
        typeAccount: TypeAccount,
        currentDateTime: LocalDateTime,
        validity: LocalDateTime
    ): String {
        val issuerURL: String = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        return JWT.create()
            .withClaim("type_account", typeAccount.toString())
            .withIssuedAt(currentDateTime.toDate())
            .withExpiresAt(validity.toDate())
            .withSubject(username)
            .withIssuer(issuerURL)
            .sign(algorithm)
            .trim()
    }

    fun getRefreshToken(
        username: String,
        typeAccount: TypeAccount,
        currentLocalDateTime: LocalDateTime
    ): String {
        val validRefreshToken = currentLocalDateTime.plusDays(tokenExpiresAt)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .let { Date.from(it) }
        return JWT.create()
            .withClaim("type_account", typeAccount.toString())
            .withExpiresAt(validRefreshToken)
            .withSubject(username)
            .sign(algorithm)
            .trim()
    }

    fun getAuthentication(
        token: String
    ): Authentication {
        val decodedJWT: DecodedJWT = decodedToken(token = token)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(decodedJWT.subject)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun decodedToken(
        token: String
    ): DecodedJWT {
        val algorithm = Algorithm.HMAC256(secretKey.toByteArray())
        val verify: JWTVerifier = JWT.require(algorithm).build()
        return verify.verify(token)
    }

    fun resolveToken(
        req: HttpServletRequest
    ): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (!bearerToken.isNullOrBlank() && bearerToken.startsWith(prefix = "Bearer ")) {
            bearerToken.substring(startIndex = "Bearer ".length)
        } else {
            null
        }
    }

    fun validateToken(
        token: String
    ): Boolean {
        try {
            val decodedJWT = decodedToken(token = token)
            if (decodedJWT.expiresAt.before(Date())) {
                throw InvalidJwtAuthenticationException("Token has expired")
            }
            return true
        } catch (e: TokenExpiredException) {
            throw e
        } catch (e: Exception) {
            throw InvalidJwtAuthenticationException("Expired or invalid JWT token!")
        }
    }
}
