package settlement.kotlin.service.order.req

import settlement.kotlin.service.order.model.Payment
import settlement.kotlin.service.owner.model.OwnerId

data class CreateOrderCommand(
    val ownerId: OwnerId,
    val payments: List<Payment>
)
