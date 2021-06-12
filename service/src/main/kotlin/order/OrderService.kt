package settlement.kotlin.service.order

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.order.event.CreateOrderEvent
import settlement.kotlin.service.order.event.ModifyOrderEvent
import settlement.kotlin.service.order.event.QueryOrderResponse
import settlement.kotlin.service.order.model.OrderId
import settlement.kotlin.service.order.model.Price
import settlement.kotlin.service.order.req.CreateOrderCommand
import settlement.kotlin.service.order.req.ModifyOrderCommand
import settlement.kotlin.service.order.req.QueryOrderCommand

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
                    OrderEntity(ownerId = ownerId.value, totalPrice = price.value)
                ).id.let(::OrderId)
            }
        )

    @Transactional
    fun modifyOrder(req: ModifyOrderCommand): ModifyOrderEvent =
        OrderHandler.modify(
            command = req,
            findOrderEntity = { orderId ->
                orderRepository.findById(orderId.value)
            },
            canBe = { order, orderStatus ->
                order.status < orderStatus
            },
            modifyInDb = { order, orderStatus ->
                orderRepository.save(order.copy(status = orderStatus)).id.let(::OrderId)
            }
        )

    @Transactional
    fun queryOrder(req: QueryOrderCommand, pageable: Pageable): Page<QueryOrderResponse> =
        orderRepository.queryOrder(
            orderId = req.orderId?.value,
            ownerId = req.ownerId,
            orderStatus = req.orderStatus,
            startDateTime = req.startDateTime,
            endDateTime = req.endDateTime,
            pageable = pageable
        ).map {
            QueryOrderResponse(
                id = OrderId(value = it.id),
                ownerId = it.ownerId,
                totalPrice = Price(value = it.totalPrice),
                status = it.status,
                createdAt = it.createdAt
            )
        }
}
