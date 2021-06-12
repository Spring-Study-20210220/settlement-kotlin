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

interface OrderRepositoryJpa : JpaRepository<OrderEntity, Long>

interface OrderRepositorySupport {
    fun queryOrder(
        orderId: Long?,
        ownerId: Long?,
        orderStatus: String?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        pageable: Pageable
    ): PageImpl<OrderEntity>
}

@Repository
class OrderRepositorySupportImpl : QuerydslRepositorySupport(OrderEntity::class.java), OrderRepositorySupport {
    override fun queryOrder(
        orderId: Long?,
        ownerId: Long?,
        orderStatus: String?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        pageable: Pageable
    ): PageImpl<OrderEntity> {
        val query = from(QOrderEntity.orderEntity)
            .where(
                eqId(orderId), eqOwnerId(ownerId), eqStatus(orderStatus),
                between(startDateTime, endDateTime)
            )
            .fetchAll()
        return PageImpl<OrderEntity>(
            querydsl!!.applyPagination(pageable, query).fetch(),
            pageable,
            query.fetchCount()
        )
    }

    fun eqId(id: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(id)) return null
        return QOrderEntity.orderEntity.id.eq(id)
    }

    fun eqStatus(status: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(status)) return null
        return QOrderEntity.orderEntity.status.eq(status)
    }

    fun eqOwnerId(ownerId: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(ownerId)) return null
        return QOrderEntity.orderEntity.ownerId.eq(ownerId)
    }

    fun between(startTime: LocalDateTime, endTime: LocalDateTime): BooleanExpression {
        return QOrderEntity.orderEntity.createdAt.between(startTime, endTime)
    }
}
