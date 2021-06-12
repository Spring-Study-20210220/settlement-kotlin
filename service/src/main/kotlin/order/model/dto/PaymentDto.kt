package settlement.kotlin.service.order.model.dto

data class PaymentDto(
    val paymentMethod: PaymentMethod,
    val price: Int
)
