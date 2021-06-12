package settlement.kotlin.service.owner.model.info

data class CreateAccountInfo(
    val ownerId: Long,
    val bank: String,
    val bankAccount: String,
    val accountHolder: String
)
