package settlement.kotlin.service.order.model.dto

import java.time.LocalDateTime

data class OrderDto(
    val id: Long,
    val ownerId: Long,
    val totalPrice: Int,
    val status: OrderStatus,
    val createdAt: LocalDateTime
)

enum class OrderStatus {
    ORDER_READY, ORDER_TAKEN, IN_DELIVERY, DELIVERED, CANCELED
}
