package settlement.kotlin.service.owner.model.dto

data class AccountDto(
    val id: Long,
    val ownerId: Long,
    val bank: String,
    val bankAccount: String,
    val accountHolder: String
)
