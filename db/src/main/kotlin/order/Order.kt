package settlement.kotlin.db.order

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.repository.Temporal
import settlement.kotlin.db.owner.Owner
import java.time.LocalDateTime
import javax.persistence.FetchType
import javax.persistence.OneToMany

data class Order(
    val id: Long,
    val totalPrice: Int,
    val status: Status,
    @CreatedDate
    val createdAt: LocalDateTime,
    val owner: Owner,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderDetail")
    val orderDetails: MutableList<OrderDetail>
)

enum class Status(name: String){
    ORDER_READY("주문대기"),
    ORDER_ACECEPTION("주문접수"),
    ON_DELIVERY("배달중"),
    DELIVERY_COMPLETED("배달완료"),
    CANCELED("취소")
}