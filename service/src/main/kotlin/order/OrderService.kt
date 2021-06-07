package settlement.kotlin.service.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.order.Order
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.order.event.CreateOrderEvent
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.req.CreateOrderCommand

@Service
class OrderService(
    private val ownerRepository: OwnerRepository,
    private val orderRepository: OrderRepository
) {

    @Transactional
    fun createOrder(command: CreateOrderCommand): CreateOrderEvent =
        OrderHandler.create(
            command = command,
            existOwner = { ownerId ->
                ownerRepository.findById(ownerId.value).isPresent
            },
            insertInDb = { ownerId, price ->
                orderRepository.save(
                    Order(ownerId = ownerId.value, totalPrice = price.value)
                ).id.let(::OrderId)
            }
        )
}
