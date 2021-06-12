package settlement.kotlin.service.order.event

import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Price
import java.time.LocalDateTime

data class QueryOrderResponse(
    val id: OrderId,
    val ownerId: Long,
    val totalPrice: Price,
    val status: OrderStatus,
    val createdAt: LocalDateTime
)
