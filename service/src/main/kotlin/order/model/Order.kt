package settlement.kotlin.service.order.model

import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.service.owner.model.OwnerId
import java.time.LocalDateTime

data class Order(
    val id: OrderId,
    val ownerId: OwnerId,
    val totalPrice: Price,
    val status: OrderStatus,
    val createdAt: LocalDateTime
)
