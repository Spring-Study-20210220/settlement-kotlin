package settlement.kotlin.db.owner

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByOwnerIdAndBankAndBankAccount(ownerId: Long, bank: String, bankAccount: String): Account?
}
