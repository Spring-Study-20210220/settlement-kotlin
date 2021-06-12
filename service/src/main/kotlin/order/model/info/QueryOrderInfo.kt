package settlement.kotlin.service.order.model.info

import settlement.kotlin.service.order.model.dto.OrderStatus
import java.time.LocalDateTime

data class QueryOrderInfo(
    val orderId: Long?,
    val ownerId: Long?,
    val orderStatus: OrderStatus?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)
