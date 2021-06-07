package settlement.kotlin.service.owner.req

data class CreateAccountRequest(
    val ownerId: Long,
    val bank: String,
    val bankAccount: String,
    val accountHolder: String
)
