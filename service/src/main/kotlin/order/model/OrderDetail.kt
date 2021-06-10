package settlement.kotlin.service.order.model

data class OrderDetail(
    val id: OrderDetailId,
    val orderId: OrderId,
    val paymentMethod: PaymentMethod,
    val price: Price
)
