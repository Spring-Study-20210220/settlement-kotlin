package settlement.kotlin.db.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun findByIdAndIsAdmin(id: Long, isAdmin: Boolean): UserEntity?
}
