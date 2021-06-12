package settlement.kotlin.service.order.req

import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.service.order.model.OrderId
import java.time.LocalDateTime

data class QueryOrderCommand(
    val orderId: OrderId?,
    val ownerId: Long?,
    val orderStatus: OrderStatus?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)