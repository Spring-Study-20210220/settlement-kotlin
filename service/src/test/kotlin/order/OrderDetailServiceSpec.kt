package settlement.kotlin.service.order

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import settlement.kotlin.db.order.OrderDetailEntity
import settlement.kotlin.db.order.OrderDetailRepository
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.service.order.model.info.CreateOrderDetailInfo
import settlement.kotlin.service.order.model.dto.OrderStatus
import settlement.kotlin.service.order.model.dto.PaymentDto
import settlement.kotlin.service.order.model.dto.PaymentMethod
import java.time.LocalDateTime
import java.util.Optional

class OrderDetailServiceSpec : FeatureSpec() {
    private val orderDetailRepository: OrderDetailRepository = mockk()
    private val orderRepository: OrderRepository = mockk()
    private val orderDetailService: OrderDetailService = OrderDetailService(
        orderDetailRepository = orderDetailRepository,
        orderRepository = orderRepository
    )
    private val existOrder = OrderEntity(
        id = 1L,
        ownerId = 1L,
        totalPrice = 99999,
        status = OrderStatus.DELIVERED.name,
        createdAt = LocalDateTime.now()
    )
    private val existOrderDetail = OrderDetailEntity(
        id = 1L,
        orderId = 1L,
        paymentMethod = PaymentMethod.CASH.name,
        price = 99999
    )
    private val orderIdSlot = slot<Long>()
    private val paymentMethodSlot = slot<String>()
    private val orderDetailSlot = slot<OrderDetailEntity>()

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
                OrderDetailEntity(id = id, orderId = orderId, paymentMethod = paymentMethod, price = price)
            }

            scenario("해당하는 주문이 없으면, 예외를 발생시킨다.") {
                val req = CreateOrderDetailInfo(
                    orderId = 404L,
                    payment = PaymentDto(paymentMethod = PaymentMethod.CREDIT_CARD, price = 10000,)
                )
                shouldThrowExactly<RuntimeException> {
                    orderDetailService.createOrderDetail(req)
                }
            }

            scenario("중복된 주문-결제수단이 존재할 경우, 예외를 발생시킨다.") {
                val req = CreateOrderDetailInfo(
                    orderId = 1L,
                    payment = PaymentDto(paymentMethod = PaymentMethod.CASH, price = 10000)
                )

                shouldThrowExactly<RuntimeException> {
                    orderDetailService.createOrderDetail(req)
                }
            }

            scenario("정상적으로 상세 주문을 생성한다.") {
                val req = CreateOrderDetailInfo(
                    orderId = 1L,
                    payment = PaymentDto(
                        paymentMethod = PaymentMethod.POINT,
                        price = 10000
                    )
                )

                val result = orderDetailService.createOrderDetail(req)

                result.orderId shouldBe req.orderId
                result.payment shouldBe req.payment
            }
        }

        feature("주문 상세 조회 기능") {
            every {
                orderDetailRepository.findByOrderId(capture(orderIdSlot))
            }.answers {
                if (orderIdSlot.captured == existOrder.id) listOf(existOrderDetail)
                else emptyList()
            }

            scenario("정상적으로 조회한다.") {
                val result = orderDetailService.query(1L)

                result.isNotEmpty()
                result[0].id shouldBe 1L
                result[0].orderId shouldBe 1L
            }
        }
    }
}
