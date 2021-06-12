package settlement.kotlin.service.order.model.info

import settlement.kotlin.service.order.model.dto.PaymentDto

data class CreateOrderDetailInfo(
    val orderId: Long,
    val payment: PaymentDto
)
