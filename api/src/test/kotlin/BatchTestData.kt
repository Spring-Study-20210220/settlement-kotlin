import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.owner.OwnerEntity
import settlement.kotlin.service.order.model.dto.OrderStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BatchTestData {
    fun getTestOwners() = listOf<OwnerEntity>(
        OwnerEntity(name = "testOwenr1", email = "test1@test1", phoneNumber = "010-1111-1111", accounts = emptyList()),
        OwnerEntity(name = "testOwenr2", email = "test2@test2", phoneNumber = "010-2222-2222", accounts = emptyList()),
        OwnerEntity(name = "testOwenr3", email = "test3@test3", phoneNumber = "010-3333-3333", accounts = emptyList()),
        OwnerEntity(name = "testOwenr4", email = "test4@test4", phoneNumber = "010-4444-4444", accounts = emptyList()),
        OwnerEntity(name = "testOwenr5", email = "test5@test5", phoneNumber = "010-5555-5555", accounts = emptyList())
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
        OrderEntity(ownerId = 2L, totalPrice = 30000, status = OrderStatus.DELIVERED.name, createdAt = time3),
        OrderEntity(ownerId = 2L, totalPrice = 45000, status = OrderStatus.DELIVERED.name, createdAt = time4),
        OrderEntity(ownerId = 3L, totalPrice = 20000, status = OrderStatus.DELIVERED.name, createdAt = time4),
        OrderEntity(ownerId = 3L, totalPrice = 10000, status = OrderStatus.DELIVERED.name, createdAt = time1),
        OrderEntity(ownerId = 4L, totalPrice = 15000, status = OrderStatus.DELIVERED.name, createdAt = time2),
        OrderEntity(ownerId = 4L, totalPrice = 30000, status = OrderStatus.DELIVERED.name, createdAt = time3),
        OrderEntity(ownerId = 5L, totalPrice = 45000, status = OrderStatus.DELIVERED.name, createdAt = time4),
        OrderEntity(ownerId = 5L, totalPrice = 20000, status = OrderStatus.DELIVERED.name, createdAt = time4),
    )
}
