package settlement.kotlin.db.order

import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "orders")
data class OrderEntity(
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

object OrderSpec {
    fun all(): Specification<OrderEntity> {
        return Specification { _, _, builder ->
            builder.conjunction()
        }
    }

    fun id(id: Long): Specification<OrderEntity> {
        return Specification { root, _, builder ->
            builder.equal(root.get<Long>("id"), id)
        }
    }

    fun ownerId(ownerId: Long): Specification<OrderEntity> {
        return Specification { root, _, builder ->
            builder.equal(root.get<Long>("ownerId"), ownerId)
        }
    }

    fun after(time: LocalDateTime): Specification<OrderEntity> {
        return Specification { root, _, builder ->
            builder.greaterThanOrEqualTo(root.get("createdAt"), time)
        }
    }

    fun before(time: LocalDateTime): Specification<OrderEntity> {
        return Specification { root, _, builder ->
            builder.lessThanOrEqualTo(root.get("createdAt"), time)
        }
    }
}
