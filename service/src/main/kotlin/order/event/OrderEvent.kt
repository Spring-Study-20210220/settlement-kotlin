package settlement.kotlin.service.order.event

import settlement.kotlin.service.Event
import settlement.kotlin.service.order.model.OrderId

interface OrderEvent : Event {
    val orderId: OrderId
}
