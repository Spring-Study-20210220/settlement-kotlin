package settlement.kotlin.service.order

import org.springframework.stereotype.Service
import settlement.kotlin.db.order.OrderDetailEntity
import settlement.kotlin.db.order.OrderDetailRepository
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.service.order.model.info.CreateOrderDetailInfo
import settlement.kotlin.service.order.model.dto.OrderDetailDto
import settlement.kotlin.service.order.model.dto.PaymentDto
import settlement.kotlin.service.order.model.dto.PaymentMethod

@Service
class OrderDetailService(
    private val orderDetailRepository: OrderDetailRepository,
    private val orderRepository: OrderRepository
) {

    fun createOrderDetail(info: CreateOrderDetailInfo): OrderDetailDto {
        orderRepository.findById(info.orderId)
            .orElseThrow(::RuntimeException)
        orderDetailRepository.findByOrderIdAndPaymentMethod(
            info.orderId,
            info.payment.paymentMethod.name
        )?.let {
            throw RuntimeException()
        }
        return orderDetailRepository.save(
            OrderDetailEntity(
                orderId = info.orderId,
                paymentMethod = info.payment.paymentMethod.name,
                price = info.payment.price
            )
        ).let {
            OrderDetailDto(
                id = it.id,
                orderId = it.orderId,
                payment = PaymentDto(
                    paymentMethod = PaymentMethod.valueOf(it.paymentMethod),
                    price = it.price
                )
            )
        }
    }

    fun query(orderId: Long): List<OrderDetailDto> =
        orderDetailRepository.findByOrderId(orderId)
            .map {
                OrderDetailDto(
                    id = it.id,
                    orderId = orderId,
                    payment = PaymentDto(
                        paymentMethod = PaymentMethod.valueOf(it.paymentMethod),
                        price = it.price
                    )
                )
            }
}
