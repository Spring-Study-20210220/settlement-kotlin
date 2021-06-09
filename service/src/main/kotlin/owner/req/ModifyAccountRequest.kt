package settlement.kotlin.service.owner.req

data class ModifyAccountRequest(
    val ownerId: Long,
    val id: Long,
    val bank: String,
    val bankAccount: String,
    val accountHolder: String
)
