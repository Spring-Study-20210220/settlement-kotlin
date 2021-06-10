package settlement.kotlin.db.order

import org.springframework.data.jpa.repository.JpaRepository

interface OrderDetailRepository : JpaRepository<OrderDetailEntity, OrderEntity> {
    fun findByOrderIdAndPaymentMethod(orderId: Long, paymentMethod: String): OrderDetailEntity?
    fun findByOrderId(orderId: Long): List<OrderDetailEntity>
}
