package settlement.kotlin.service.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.order.Order
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.order.event.CreateOrderEvent
import settlement.kotlin.service.order.event.ModifyOrderEvent
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.req.CreateOrderCommand
import settlement.kotlin.service.order.req.ModifyOrderCommand

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

    @Transactional
    fun modifyOrder(req: ModifyOrderCommand): ModifyOrderEvent =
        OrderHandler.modify(
            command = req,
            findOrder = { orderId ->
                orderRepository.findById(orderId.value)
            },
            canBe = { order, orderStatus ->
                order.status < orderStatus
            },
            modifyInDb = { order, orderStatus ->
                orderRepository.save(order.copy(status = orderStatus)).id.let(::OrderId)
            }
        )
}
