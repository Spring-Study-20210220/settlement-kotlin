package settlement.kotlin.service.order.model.dto

data class OrderDetailDto(
    val id: Long,
    val orderId: Long,
    val payment: PaymentDto
)

enum class PaymentMethod {
    CASH, CREDIT_CARD, POINT
}
