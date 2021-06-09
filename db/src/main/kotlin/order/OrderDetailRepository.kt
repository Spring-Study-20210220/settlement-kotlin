package settlement.kotlin.db.order

import org.springframework.data.jpa.repository.JpaRepository

interface OrderDetailRepository : JpaRepository<OrderDetail, Order> {
    fun findByOrderIdAndPaymentMethod(orderId: Long, paymentMethod: String): OrderDetail?
}
