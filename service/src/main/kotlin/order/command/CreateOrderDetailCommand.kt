package settlement.kotlin.service.order.req

import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.PaymentMethod
import settlement.kotlin.service.order.model.Price

data class CreateOrderDetailCommand(
    val orderId: OrderId,
    val paymentMethod: PaymentMethod,
    val price: Price
)
