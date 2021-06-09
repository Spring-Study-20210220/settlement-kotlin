package settlement.kotlin.service.order.event

import settlement.kotlin.service.Event
import settlement.kotlin.service.order.model.OrderId
import java.time.Instant

abstract class OrderEvent(
    open val orderId: OrderId,
    override val timestamp: Instant = Instant.now()
) : Event
