package settlement.kotlin.service.order

import settlement.kotlin.db.order.Order
import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.db.owner.Owner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OrderServiceTestData {
    fun getTestOwners() = listOf<Owner>(
        Owner(name = "testOwenr1", email = "test1@test1", phoneNumber = "010-1111-1111"),
        Owner(name = "testOwenr2", email = "test2@test2", phoneNumber = "010-2222-2222")
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
    fun getTestOrders() = listOf<Order>(
        Order(ownerId = 1L, totalPrice = 10000, status = OrderStatus.DELIVERED, createdAt = time1),
        Order(ownerId = 1L, totalPrice = 15000, status = OrderStatus.CANCELED, createdAt = time2),
        Order(ownerId = 1L, totalPrice = 30000, status = OrderStatus.DELIVERED, createdAt = time3),
        Order(ownerId = 1L, totalPrice = 45000, status = OrderStatus.DELIVERED, createdAt = time4),
        Order(ownerId = 1L, totalPrice = 20000, status = OrderStatus.DELIVERED, createdAt = time4),
        Order(ownerId = 2L, totalPrice = 10000, status = OrderStatus.DELIVERED, createdAt = time1),
        Order( ownerId = 2L, totalPrice = 15000, status = OrderStatus.CANCELED, createdAt = time2),
        Order(ownerId = 2L, totalPrice = 30000, status = OrderStatus.DELIVERED, createdAt = time3),
        Order(ownerId = 2L, totalPrice = 45000, status = OrderStatus.DELIVERED, createdAt = time4),
        Order(ownerId = 2L, totalPrice = 20000, status = OrderStatus.DELIVERED, createdAt = time4),
    )

}