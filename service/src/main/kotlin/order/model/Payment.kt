package settlement.kotlin.service.order.model

data class Payment(
    val paymentMethod: PaymentMethod,
    val price: Price
)

enum class PaymentMethod {
    CASH, CREDIT_CARD, POINT
}

data class Price(
    val value: Int
)
