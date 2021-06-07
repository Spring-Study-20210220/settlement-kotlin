package settlement.kotlin.service.order.event

import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Payment
import settlement.kotlin.service.order.model.Price
import java.time.Instant

data class CreateOrderEvent(
    override val orderId: OrderId,
    override val timestamp: Instant = Instant.now(),
    val totalPrice: Price,
    val payments: List<Payment>
) : OrderEvent
