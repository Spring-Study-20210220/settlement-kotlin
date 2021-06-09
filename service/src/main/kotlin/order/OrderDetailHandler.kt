package settlement.kotlin.service.order

import settlement.kotlin.service.order.event.CreateOrderDetailEvent
import settlement.kotlin.service.order.model.OrderDetailId
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Payment
import settlement.kotlin.service.order.model.PaymentMethod
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderDetailCommand

object OrderDetailHandler {
    fun create(
        command: CreateOrderDetailCommand,
        existOrder: (OrderId) -> Boolean,
        existOrderAndPaymentMethod: (OrderId, PaymentMethod) -> Boolean,
        insertInDb: (OrderId, PaymentMethod, Price) -> OrderDetailId
    ): CreateOrderDetailEvent {
        val (orderId, paymentMethod, price) = command

        if (!existOrder(orderId)) {
            throw RuntimeException("존재하지 않는 주문 번호입니다.")
        }

        if (existOrderAndPaymentMethod(orderId, paymentMethod)) {
            throw RuntimeException("중복된 주문-결제수단이 존재합니다.")
        }

        val orderDetailId = insertInDb(orderId, paymentMethod, price)

        return CreateOrderDetailEvent(
            orderDetailId = orderDetailId,
            orderId = orderId,
            payment = Payment(paymentMethod, price)
        )
    }
}
