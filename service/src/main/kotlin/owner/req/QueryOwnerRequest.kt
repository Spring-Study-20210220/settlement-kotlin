package settlement.kotlin.service.owner.req

data class QueryOwnerRequest(
    val ownerId: Long?,
    val ownerName: String?,
    val ownerEmail: String?
)
