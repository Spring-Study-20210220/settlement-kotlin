package settlement.kotlin.db.owner

import javax.persistence.*

@Entity(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    val owner: Owner,
    var bank: String,
    var bankAccount: String,
    var accountHolder: String
) {
    fun modifyAccountInfo(bank: String, bankAccount: String, accountHolder: String) {
        this.bank = bank
        this.bankAccount = bankAccount
        this.accountHolder = accountHolder
    }
}
