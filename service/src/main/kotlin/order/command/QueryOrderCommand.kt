package settlement.kotlin.service.order.req

import org.springframework.data.jpa.domain.Specification
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderSpec
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.owner.model.OwnerId
import java.time.LocalDateTime

data class QueryOrderCommand(
    val orderId: OrderId?,
    val ownerId: OwnerId?,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?
) {

    fun toSpec(): Specification<OrderEntity> =
        OrderSpec.all()
            .and(orderId?.value?.let { OrderSpec.id(it) })
            .and(ownerId?.value?.let { OrderSpec.ownerId(it) })
            .and(startAt?.let { OrderSpec.after(it) })
            .and(endAt?.let { OrderSpec.before(it) })
}
