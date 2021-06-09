package settlement.kotlin.service.order.event

import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.service.order.model.OrderId

class ModifyOrderEvent(
    override val orderId: OrderId,
    val orderStatus: OrderStatus
) : OrderEvent(orderId)
