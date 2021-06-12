package settlement.kotlin.order.res

import settlement.kotlin.service.order.model.dto.PaymentDto

data class CreateOrderResponse(
    val orderId: Long,
    val ownerId: Long,
    val totalPrice: Int,
    val paymentDtos: List<PaymentDto>
)
