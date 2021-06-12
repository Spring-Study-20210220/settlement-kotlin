package settlement.kotlin.service.order

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.domain.PageRequest
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.OwnerEntity
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.DatabaseTest
import settlement.kotlin.service.order.model.info.CreateOrderInfo
import settlement.kotlin.service.order.model.info.ModifyOrderInfo
import settlement.kotlin.service.order.model.dto.OrderStatus
import settlement.kotlin.service.order.model.dto.PaymentDto
import settlement.kotlin.service.order.model.dto.PaymentMethod
import settlement.kotlin.service.order.model.info.QueryOrderInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DatabaseTest
class OrderServiceSpec(
    private val ownerRepository: OwnerRepository,
    private val orderRepository: OrderRepository,
    private val orderDetailService: OrderDetailService
) : FeatureSpec() {

    private val orderService = OrderService(
        ownerRepository = ownerRepository,
        orderRepository = orderRepository,
        orderDetailService = orderDetailService
    )

    init {
        beforeTest {
            if (ownerRepository.findById(1L).isEmpty) {
                ownerRepository.save(owner)
            }
            if (orderRepository.findById(1L).isEmpty) {
                orderRepository.save(order)
            }
        }

        feature("주문 생성 기능") {
            scenario("존재하지 않는 업주라면, 예외가 발생한다.") {

                shouldThrowExactly<RuntimeException> {
                    orderService.createOrder(
                        createCommand.copy(ownerId = 1000)
                    )
                }
            }

            scenario("존재하는 업주라면, 주문을 생성한다.") {
                val result = orderService.createOrder(createCommand)

                result.ownerId shouldBe 1L
                result.totalPrice shouldBe 6000
            }
        }

        feature("주문 수정 기능") {
            scenario("존재하지 않는 주문 번호라면, 예외가 발생한다.") {
                val req = ModifyOrderInfo(
                    orderId = 0L,
                    orderStatus = OrderStatus.CANCELED
                )
                shouldThrowExactly<RuntimeException> {
                    orderService.modifyOrder(req)
                }
            }

            scenario("이전 상태로의 수정 요청이라면, 예외가 발생한다.") {
                val req = ModifyOrderInfo(
                    orderId = 1L,
                    orderStatus = OrderStatus.ORDER_TAKEN
                )
                shouldThrowExactly<RuntimeException> {
                    orderService.modifyOrder(req)
                }
            }

            scenario("정상적으로 주문을 수정한다.") {
                val req = ModifyOrderInfo(
                    orderId = 1L,
                    orderStatus = OrderStatus.DELIVERED
                )

                val result = orderService.modifyOrder(req)

                result.id shouldBe req.orderId
                result.status shouldBe req.orderStatus
            }
        }

        feature("주문 조회 기능") {
            beforeTest {
                ownerRepository.deleteAll()
                orderRepository.deleteAll()
                ownerRepository.saveAll(OrderServiceTestData.getTestOwners())
                orderRepository.saveAll(OrderServiceTestData.getTestOrders())
            }
            val beforeTime1 = LocalDateTime.parse(
                "2021-06-09 20:30:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val afterTime4 = LocalDateTime.parse(
                "2021-06-14 20:30:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            scenario("특정 owner에 대해 검색한다.") {
                val req = QueryOrderInfo(
                    orderId = null,
                    ownerId = 1L,
                    orderStatus = null,
                    startDateTime = beforeTime1,
                    endDateTime = afterTime4
                )
                val pageable = PageRequest.of(0, 20)
                val result = orderService.queryOrder(req, pageable)
                result.totalElements shouldBe 5
            }
            scenario("주문 상태에 따라 검색한다.") {
                val req = QueryOrderInfo(
                    orderId = null,
                    ownerId = null,
                    orderStatus = OrderStatus.DELIVERED,
                    startDateTime = beforeTime1,
                    endDateTime = afterTime4
                )
                val pageable = PageRequest.of(0, 20)
                val result = orderService.queryOrder(req, pageable)
                result.totalElements shouldBe OrderServiceTestData.getTestOrders()
                    .filter { it.status == req.orderStatus!!.name }.size
            }
            scenario("특정 일시에 따라 검색한다.") {
                val req = QueryOrderInfo(
                    orderId = null,
                    ownerId = null,
                    orderStatus = null,
                    startDateTime = beforeTime1,
                    endDateTime = afterTime4
                )
                val pageable = PageRequest.of(0, 20)
                val result = orderService.queryOrder(req, pageable)
                result.totalElements shouldBe 10
            }
        }
    }

    private val owner = OwnerEntity(name = "test", email = "test@test.test", phoneNumber = "010-1111-1111", accounts = emptyList())
    private val createCommand =
        CreateOrderInfo(
            ownerId = 1,
            payments = listOf(
                PaymentDto(paymentMethod = PaymentMethod.CASH, 1000),
                PaymentDto(paymentMethod = PaymentMethod.CREDIT_CARD, 2000),
                PaymentDto(paymentMethod = PaymentMethod.POINT, 3000)
            )
        )

    private val order =
        OrderEntity(ownerId = 1, totalPrice = 1000, status = OrderStatus.IN_DELIVERY.name, createdAt = LocalDateTime.now())

    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}
