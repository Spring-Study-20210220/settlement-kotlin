package settlement.kotlin.service.order.event

import settlement.kotlin.service.order.model.OrderDetailId
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Payment

data class CreateOrderDetailEvent(
    val orderDetailId: OrderDetailId,
    override val orderId: OrderId,
    val payment: Payment
) : OrderEvent(orderId)
