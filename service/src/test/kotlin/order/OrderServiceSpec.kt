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
        beforeEach {
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
            scenario("조회") {
                ownerRepository.save(
                    Owner(name = "test2", email = "test2@test2.test2", phoneNumber = "010-2222-2222", accounts = emptyList())
                )
                orderRepository.saveAll(
                    listOf(
                        OrderEntity(
                            ownerId = 1,
                            totalPrice = 1000,
                            createdAt = LocalDateTime.parse(
                                "2021-05-02 23:59:59",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            )
                        ),
                        OrderEntity(
                            ownerId = 1,
                            totalPrice = 1000,
                            createdAt = LocalDateTime.parse(
                                "2021-05-02 23:59:59",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            )
                        ),
                        OrderEntity(
                            ownerId = 2,
                            totalPrice = 1000,
                            createdAt = LocalDateTime.parse(
                                "2021-05-02 23:59:59",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            )
                        )
                    )
                )

                val result = orderService.queryOrder(
                    command = QueryOrderCommand(
                        orderId = null,
                        ownerId = OwnerId(1L),
                        startAt = LocalDateTime.parse(
                            "2021-05-02 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        ),
                        endAt = LocalDateTime.parse(
                            "2021-05-02 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    ),
                    pageable = PageRequest.of(0, 10)
                )
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
        OrderEntity(
            ownerId = 1,
            totalPrice = 1000,
            status = OrderStatus.IN_DELIVERY,
            createdAt = LocalDateTime.parse(
                "2021-05-01 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
        )

    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}
