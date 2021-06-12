package settlement.kotlin.service.order

import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderStatus
import settlement.kotlin.service.order.event.CreateOrderEvent
import settlement.kotlin.service.order.event.ModifyOrderEvent
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderCommand
import settlement.kotlin.service.order.req.ModifyOrderCommand
import settlement.kotlin.service.owner.model.OwnerId
import java.util.Optional

object OrderHandler {
    fun create(
        command: CreateOrderCommand,
        existOwner: (OwnerId) -> Boolean,
        insertInDb: (OwnerId, Price) -> OrderId
    ): CreateOrderEvent {
        val ownerId = command.ownerId
        val totalPrice = Price(command.payments.map { it.price.value }.sum())

        if (!existOwner(ownerId)) throw RuntimeException()

        return CreateOrderEvent(
            orderId = insertInDb(ownerId, totalPrice),
            ownerId = ownerId,
            totalPrice = totalPrice,
            payments = command.payments,
        )
    }

    fun modify(
        command: ModifyOrderCommand,
        findOrderEntity: (OrderId) -> Optional<OrderEntity>,
        canBe: (OrderEntity, OrderStatus) -> Boolean,
        modifyInDb: (OrderEntity, OrderStatus) -> OrderId
    ): ModifyOrderEvent {
        val (orderId, orderStatus) = command

        val order = findOrderEntity(orderId).orElseThrow(::RuntimeException)
        if (!canBe(order, orderStatus)) throw RuntimeException()

        return ModifyOrderEvent(
            orderId = modifyInDb(order, orderStatus),
            orderStatus = orderStatus
        )
    }
}
