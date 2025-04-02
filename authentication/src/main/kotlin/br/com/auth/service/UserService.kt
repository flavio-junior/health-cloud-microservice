package br.com.auth.service

import br.com.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val user: UserDetails? = userRepository.findByEmail(username)
        return user ?: throw UsernameNotFoundException("$username not found")
    }

    @Transactional
    fun deleteUser(
        userLoggedId: Long
    ) {
        userRepository.deleteById(userLoggedId)
    }
}
