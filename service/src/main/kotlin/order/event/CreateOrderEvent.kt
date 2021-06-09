package settlement.kotlin.service.order.event

import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Payment
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.owner.model.OwnerId

data class CreateOrderEvent(
    override val orderId: OrderId,
    val ownerId: OwnerId,
    val totalPrice: Price,
    val payments: List<Payment>
) : OrderEvent(orderId)
