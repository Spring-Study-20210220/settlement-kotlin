package settlement.kotlin.service.owner.res

data class CreateAccountResponse(
    val id: Long,
    val ownerId: Long,
    val bank: String,
    val bankAccount: String,
    val accountHolder: String
)
