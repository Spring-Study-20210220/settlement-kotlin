package settlement.kotlin.service.owner.res

data class CreateOwnerResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String
)
