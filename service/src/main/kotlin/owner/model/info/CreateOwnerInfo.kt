package settlement.kotlin.service.owner.model.info

data class CreateOwnerInfo(
    val userId: Long,
    val ownerName: String,
    val ownerEmail: String,
    val ownerPhoneNumber: String,
)
