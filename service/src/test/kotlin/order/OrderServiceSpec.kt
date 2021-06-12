package settlement.kotlin.service.order

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.springframework.data.domain.PageRequest
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.DatabaseTest
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Payment
import settlement.kotlin.service.order.model.PaymentMethod
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderCommand
import settlement.kotlin.service.order.req.ModifyOrderCommand
import settlement.kotlin.service.order.req.QueryOrderCommand
import settlement.kotlin.service.owner.model.OwnerId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DatabaseTest
class OrderServiceSpec(
    private val ownerRepository: OwnerRepository,
    private val orderRepository: OrderRepository
) : FeatureSpec() {

    private val orderService = OrderService(
        ownerRepository = ownerRepository,
        orderRepository = orderRepository
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
                        createCommand.copy(ownerId = OwnerId(1000))
                    )
                    delay(1000)
                }
            }

            scenario("존재하는 업주라면, 주문을 생성한다.") {
                val result = orderService.createOrder(createCommand)

                result.ownerId.value shouldBe 1L
                result.totalPrice.value shouldBe 6000
                result.payments.containsAll(createCommand.payments)

                delay(1000)
            }
        }

        feature("주문 수정 기능") {
            scenario("존재하지 않는 주문 번호라면, 예외가 발생한다.") {
                val req = ModifyOrderCommand(
                    orderId = OrderId(0L),
                    orderStatus = OrderStatus.CANCELED
                )
                shouldThrowExactly<RuntimeException> {
                    orderService.modifyOrder(req)
                }
            }

            scenario("이전 상태로의 수정 요청이라면, 예외가 발생한다.") {
                val req = ModifyOrderCommand(
                    orderId = OrderId(1L),
                    orderStatus = OrderStatus.ORDER_TAKEN
                )
                shouldThrowExactly<RuntimeException> {
                    orderService.modifyOrder(req)
                }
            }

            scenario("정상적으로 주문을 수정한다.") {
                val req = ModifyOrderCommand(
                    orderId = OrderId(1L),
                    orderStatus = OrderStatus.DELIVERED
                )

                val result = orderService.modifyOrder(req)

                result.orderId shouldBe req.orderId
                result.orderStatus shouldBe req.orderStatus
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
                val req = QueryOrderCommand(
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
                val req = QueryOrderCommand(
                    orderId = null,
                    ownerId = null,
                    orderStatus = OrderStatus.DELIVERED,
                    startDateTime = beforeTime1,
                    endDateTime = afterTime4
                )
                val pageable = PageRequest.of(0, 20)
                val result = orderService.queryOrder(req, pageable)
                result.totalElements shouldBe 8
            }
            scenario("특정 일시에 따라 검색한다.") {
                val req = QueryOrderCommand(
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

    private val owner = Owner(name = "test", email = "test@test.test", phoneNumber = "010-1111-1111", accounts = emptyList())
    private val createCommand =
        CreateOrderCommand(
            ownerId = OwnerId(value = 1),
            payments = listOf(
                Payment(paymentMethod = PaymentMethod.CASH, Price(1000)),
                Payment(paymentMethod = PaymentMethod.CREDIT_CARD, Price(2000)),
                Payment(paymentMethod = PaymentMethod.POINT, Price(3000))
            )
        )

    private val order =
        OrderEntity(ownerId = 1, totalPrice = 1000, status = OrderStatus.IN_DELIVERY, createdAt = LocalDateTime.now())

    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}
