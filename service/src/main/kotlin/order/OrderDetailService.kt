package settlement.kotlin.service.order

import org.springframework.stereotype.Service
import settlement.kotlin.db.order.OrderDetail
import settlement.kotlin.db.order.OrderDetailRepository
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.service.order.event.CreateOrderDetailEvent
import settlement.kotlin.service.order.model.OrderDetailId
import settlement.kotlin.service.order.req.CreateOrderDetailCommand

@Service
class OrderDetailService(
    private val orderDetailRepository: OrderDetailRepository,
    private val orderRepository: OrderRepository
) {
    fun createOrderDetail(req: CreateOrderDetailCommand): CreateOrderDetailEvent =
        OrderDetailHandler.create(
            command = req,
            existOrder = {
                orderRepository.findById(it.value).isPresent
            },
            existOrderAndPaymentMethod = { orderId, paymentMethod ->
                orderDetailRepository
                    .findByOrderIdAndPaymentMethod(orderId.value, paymentMethod.name) != null
            },
            insertInDb = { orderId, paymentMethod, price ->
                orderDetailRepository.save(
                    OrderDetail(
                        orderId = orderId.value,
                        paymentMethod = paymentMethod.name,
                        price = price.value
                    )
                ).let {
                    OrderDetailId(it.id)
                }
            }
        )
}
