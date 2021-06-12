package settlement.kotlin.service.order.model.info

import settlement.kotlin.service.order.model.dto.PaymentDto

data class CreateOrderInfo(
    val ownerId: Long,
    val payments: List<PaymentDto>
)
