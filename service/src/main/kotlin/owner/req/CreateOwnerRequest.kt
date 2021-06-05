package settlement.kotlin.service.owner.req

data class CreateOwnerRequest(
    val userId: Long,
    val ownerName: String,
    val ownerEmail: String,
    val ownerPhoneNumber: String,
)
