package settlement.kotlin.service.order.model

data class Payment(
    val paymentMethod: PaymentMethod,
    val price: Price
)
