import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.order.QuerySettleProjection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DatabaseTest
class OrderRepositoryTest(
    private val orderRepository: OrderRepository
) : StringSpec() {
    val time1 = LocalDateTime.parse(
        "2021-06-10 12:30:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    val time2 = LocalDateTime.parse(
        "2021-06-11 12:30:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

    init {
        beforeTest {
            listOf<OrderEntity>(
                OrderEntity(ownerId = 1L, totalPrice = 10000, status = "delivered", createdAt = time1),
                OrderEntity(ownerId = 1L, totalPrice = 15000, status = "delivered", createdAt = time2),
                OrderEntity(ownerId = 2L, totalPrice = 30000, status = "delivered", createdAt = time1),
                OrderEntity(ownerId = 2L, totalPrice = 45000, status = "delivered", createdAt = time1)
            ).let {
                orderRepository.saveAll(it)
            }

        }
        "점주별 정산금을 집계한다"{
            orderRepository.querySettleGroupByOwnerId(time1, time2)
                .forEach {
                    println("${it.getOwnerId()} ${it.getSettleMoney()}")
                }
        }
        "심플 query테스트"{
            orderRepository.testQuery()
                .forEach {
                    println(it.getOwnerId())
                    println(it.getTotalPrice())
                }
        }
        "query projection 인터페이스 테스트"{
            orderRepository.testProjectionQuery1()
                .forEach {
                    println(it.getOwnerId())
                    println(it.getSettleMoney())
                }
        }
        "query projection 생성자 테스트"{
            orderRepository.testProjectionQuery2()
                .forEach(::print)
        }

    }
}

