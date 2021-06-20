package settlement.kotlin.db.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderRepositoryJpa : JpaRepository<OrderEntity, Long> {
    @Query(
        "select O.owner_id as ownerid, sum(O.total_price) as settlemoney " +
                "from orders as O " +
                "where O.created_at>=:start and O.created_at<:end " +
                "group by O.owner_id",
        nativeQuery = true
    )
    fun querySettleGroupByOwnerId(start: LocalDateTime, end: LocalDateTime): List<QuerySettleProjection>

    @Query("select o.owner_id as ownerid, o.total_price as totalprice from orders as o", nativeQuery = true)
    fun testQuery(): List<QuerySimpleProjection>

    @Query(
        "select o.owner_id as ownerid, sum(o.total_price) as settlemoney " +
                "from orders as o " +
                "group by o.owner_id",
        nativeQuery = true
    )
    fun testProjectionQuery1(): List<QuerySettleProjection>

    @Query(
        "select new settlement.kotlin.db.order.QueryTestProjectionByConstructor(" +
                "o.owner_id as ownerId, " +
                "sum(o.total_price) as settleMoney " +
                ") " +
                "from orders as o " +
                "group by o.owner_id",
        nativeQuery = true
    )
    fun testProjectionQuery2(): List<QueryTestProjectionByConstructor>
}

interface OrderRepository : OrderRepositoryJpa, OrderRepositorySupport

interface QuerySimpleProjection {
    fun getOwnerId(): Long
    fun getTotalPrice(): Int
}

interface QuerySettleProjection {
    fun getOwnerId(): Long
    fun getSettleMoney(): Int
}

interface QueryTestProjectionByInterface {
    fun getOwnerId(): Long
    fun getSettleMoney(): Int
}

data class QueryTestProjectionByConstructor(
    val ownerId: Long,
    val settleMoney: Int
)



