package settlement.kotlin.db.order

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import java.time.LocalDateTime

interface OrderRepository : OrderRepositoryJpa, OrderRepositorySupport

interface OrderRepositoryJpa : JpaRepository<Order, Long>

interface OrderRepositorySupport {
    fun queryOrder(
        orderId: Long?,
        ownerId: Long?,
        orderStatus: OrderStatus?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        pageable: Pageable
    ): PageImpl<Order>
}

@Repository
class OrderRepositorySupportImpl : QuerydslRepositorySupport(Order::class.java), OrderRepositorySupport {
    override fun queryOrder(
        orderId: Long?,
        ownerId: Long?,
        orderStatus: OrderStatus?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        pageable: Pageable
    ): PageImpl<Order> {
        val query = from(QOrder.order)
            .where(
                eqId(orderId), eqOwnerId(ownerId), eqStatus(orderStatus),
                between(startDateTime, endDateTime)
            )
            .fetchAll()
        return PageImpl<Order>(
            querydsl!!.applyPagination(pageable, query).fetch(),
            pageable,
            query.fetchCount()
        )
    }

    fun eqId(id: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(id)) return null
        return QOrder.order.id.eq(id)
    }

    fun eqStatus(status: OrderStatus?): BooleanExpression? {
        if (ObjectUtils.isEmpty(status)) return null
        return QOrder.order.status.eq(status)
    }

    fun eqOwnerId(ownerId: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(ownerId)) return null
        return QOrder.order.ownerId.eq(ownerId)
    }

    fun between(startTime: LocalDateTime, endTime: LocalDateTime): BooleanExpression {
        return QOrder.order.createdAt.between(startTime, endTime)
    }


}
