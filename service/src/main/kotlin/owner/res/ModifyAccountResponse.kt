package settlement.kotlin.service.owner.res

data class ModifyAccountResponse(
    val ownerId: Long,
    val id: Long,
    val bank: String,
    val bankAccount: String,
    val accountHolder: String
)
