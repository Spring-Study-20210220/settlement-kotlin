package settlement.kotlin.db.order

import javax.persistence.*

data class OrderDetail(
    val id: Long,
    @ManyToOne
    @JoinColumn(name="order_id")
    val order: Order,
    val price: Int,
    @Enumerated(EnumType.STRING)
    val payment: Payment
)

enum class Payment(name: String){
    CASH("현금"),
    CARD("카드"),
    COUPON("쿠폰"),
    PAY("페이")
}