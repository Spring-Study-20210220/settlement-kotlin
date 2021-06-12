package settlement.kotlin.service.order

import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.owner.OwnerEntity
import settlement.kotlin.service.order.model.dto.OrderStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OrderServiceTestData {
    fun getTestOwners() = listOf<OwnerEntity>(
        OwnerEntity(name = "testOwenr1", email = "test1@test1", phoneNumber = "010-1111-1111", accounts = emptyList()),
        OwnerEntity(name = "testOwenr2", email = "test2@test2", phoneNumber = "010-2222-2222", accounts = emptyList())
    )
    val time1 = LocalDateTime.parse(
        "2021-06-10 12:30:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    val time2 = LocalDateTime.parse(
        "2021-06-11 12:30:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    val time3 = LocalDateTime.parse(
        "2021-06-12 12:30:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    val time4 = LocalDateTime.parse(
        "2021-06-13 12:30:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    fun getTestOrders() = listOf<OrderEntity>(
        OrderEntity(ownerId = 1L, totalPrice = 10000, status = OrderStatus.DELIVERED.name, createdAt = time1),
        OrderEntity(ownerId = 1L, totalPrice = 15000, status = OrderStatus.DELIVERED.name, createdAt = time2),
        OrderEntity(ownerId = 1L, totalPrice = 30000, status = OrderStatus.DELIVERED.name, createdAt = time3),
        OrderEntity(ownerId = 1L, totalPrice = 45000, status = OrderStatus.DELIVERED.name, createdAt = time4),
        OrderEntity(ownerId = 1L, totalPrice = 20000, status = OrderStatus.DELIVERED.name, createdAt = time4),
        OrderEntity(ownerId = 2L, totalPrice = 10000, status = OrderStatus.DELIVERED.name, createdAt = time1),
        OrderEntity(ownerId = 2L, totalPrice = 15000, status = OrderStatus.DELIVERED.name, createdAt = time2),
        OrderEntity(ownerId = 2L, totalPrice = 30000, status = OrderStatus.DELIVERED.name, createdAt = time3),
        OrderEntity(ownerId = 2L, totalPrice = 45000, status = OrderStatus.DELIVERED.name, createdAt = time4),
        OrderEntity(ownerId = 2L, totalPrice = 20000, status = OrderStatus.DELIVERED.name, createdAt = time4),
    )
}
