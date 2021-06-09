package settlement.kotlin.service.order

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import settlement.kotlin.db.order.Order
import settlement.kotlin.db.order.OrderDetail
import settlement.kotlin.db.order.OrderDetailRepository
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.PaymentMethod
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderDetailCommand
import java.time.LocalDateTime
import java.util.Optional

class OrderDetailServiceSpec : FeatureSpec() {
    private val orderDetailRepository: OrderDetailRepository = mockk()
    private val orderRepository: OrderRepository = mockk()
    private val orderDetailService: OrderDetailService = OrderDetailService(
        orderDetailRepository = orderDetailRepository,
        orderRepository = orderRepository
    )
    private val existOrder = Order(
        id = 1L,
        ownerId = 1L,
        totalPrice = 99999,
        status = OrderStatus.DELIVERED,
        createdAt = LocalDateTime.now()
    )
    private val existOrderDetail = OrderDetail(
        id = 1L,
        orderId = 1L,
        paymentMethod = PaymentMethod.CASH.name,
        price = 99999
    )
    private val orderIdSlot = slot<Long>()
    private val paymentMethodSlot = slot<String>()
    private val orderDetailSlot = slot<OrderDetail>()

    init {
        feature("상세 주문 생성") {
            every { orderRepository.findById(capture(orderIdSlot)) }
                .answers {
                    if (orderIdSlot.captured == existOrder.id) Optional.of(existOrder)
                    else Optional.empty()
                }

            every {
                orderDetailRepository.findByOrderIdAndPaymentMethod(
                    orderId = capture(orderIdSlot),
                    paymentMethod = capture(paymentMethodSlot)
                )
            }.answers {
                if (orderIdSlot.captured == existOrderDetail.orderId &&
                    paymentMethodSlot.captured == existOrderDetail.paymentMethod
                ) existOrderDetail
                else null
            }

            every {
                orderDetailRepository.save(capture(orderDetailSlot))
            } answers {
                val (id, orderId, paymentMethod, price) = orderDetailSlot.captured
                OrderDetail(id = id, orderId = orderId, paymentMethod = paymentMethod, price = price)
            }

            scenario("해당하는 주문이 없으면, 예외를 발생시킨다.") {
                val req = CreateOrderDetailCommand(
                    orderId = OrderId(value = 404L),
                    paymentMethod = PaymentMethod.CREDIT_CARD,
                    price = Price(value = 10000),
                )
                shouldThrowExactly<RuntimeException> {
                    orderDetailService.createOrderDetail(req)
                }
            }

            scenario("중복된 주문-결제수단이 존재할 경우, 예외를 발생시킨다.") {
                val req = CreateOrderDetailCommand(
                    orderId = OrderId(value = 1L),
                    paymentMethod = PaymentMethod.CASH,
                    price = Price(value = 10000),
                )
                shouldThrowExactly<RuntimeException> {
                    orderDetailService.createOrderDetail(req)
                }
            }

            scenario("정상적으로 상세 주문을 생성한다.") {
                val req = CreateOrderDetailCommand(
                    orderId = OrderId(value = 1L),
                    paymentMethod = PaymentMethod.POINT,
                    price = Price(value = 10000),
                )

                val result = orderDetailService.createOrderDetail(req)

                result.orderId shouldBe req.orderId
                result.payment.paymentMethod shouldBe req.paymentMethod
                result.payment.price shouldBe req.price
            }
        }
    }
}
