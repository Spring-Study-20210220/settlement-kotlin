package settlement.kotlin.service.owner.req

data class UpdateOwnerRequest(
    val ownerId: Long,
    val name: String?,
    val phoneNumber: String?
)
