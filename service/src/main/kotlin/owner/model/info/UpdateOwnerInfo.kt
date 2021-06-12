package settlement.kotlin.service.owner.model.info

data class UpdateOwnerInfo(
    val ownerId: Long,
    val name: String?,
    val phoneNumber: String?
)
