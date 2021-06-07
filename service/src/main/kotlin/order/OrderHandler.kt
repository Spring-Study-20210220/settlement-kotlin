package settlement.kotlin.service.order

import settlement.kotlin.service.order.event.CreateOrderEvent
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderCommand
import settlement.kotlin.service.owner.model.OwnerId

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
            totalPrice = totalPrice,
            payments = command.payments,
        )
    }
}
