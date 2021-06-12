package settlement.kotlin.service.order.req

import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.service.order.model.OrderId

data class ModifyOrderCommand(
    val orderId: OrderId,
    val orderStatus: OrderStatus
)
