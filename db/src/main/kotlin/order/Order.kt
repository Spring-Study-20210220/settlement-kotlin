package settlement.kotlin.db.order

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val ownerId: Long,
    val totalPrice: Int,
    @Enumerated(value = EnumType.STRING)
    val status: OrderStatus = OrderStatus.ORDER_READY,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class OrderStatus {
    ORDER_READY, ORDER_TAKEN, IN_DELIVERY, DELIVERED, CANCELED
}
