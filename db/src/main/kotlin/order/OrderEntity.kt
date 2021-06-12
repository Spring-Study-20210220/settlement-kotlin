package settlement.kotlin.db.order

import java.time.LocalDateTime
import javax.persistence.Entity
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
    val status: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
