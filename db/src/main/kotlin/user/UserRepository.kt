package settlement.kotlin.db.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByIdAndIsAdmin(id: Long, isAdmin: Boolean): User?
}
