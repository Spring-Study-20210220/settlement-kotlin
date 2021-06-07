package settlement.kotlin.service.order

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.DatabaseTest
import settlement.kotlin.service.order.model.Payment
import settlement.kotlin.service.order.model.PaymentMethod
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderCommand
import settlement.kotlin.service.owner.model.OwnerId

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
        beforeSpec {
            if (ownerRepository.findById(1L).isEmpty) {
                ownerRepository.save(owner)
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
    }

    private val owner = Owner(name = "test", email = "test@test.test", phoneNumber = "010-1111-1111")
    private val createCommand =
        CreateOrderCommand(
            ownerId = OwnerId(value = 1),
            payments = listOf(
                Payment(paymentMethod = PaymentMethod.CASH, Price(1000)),
                Payment(paymentMethod = PaymentMethod.CREDIT_CARD, Price(2000)),
                Payment(paymentMethod = PaymentMethod.POINT, Price(3000))
            )
        )

    override fun isolationMode(): IsolationMode = IsolationMode.SingleInstance
}
