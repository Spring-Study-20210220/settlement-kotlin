package settlement.kotlin.db.order

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "order_details")
data class OrderDetailEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val orderId: Long,
    val paymentMethod: String,
    val price: Int
)
