package settlement.kotlin.service.order

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.order.model.info.CreateOrderDetailInfo
import settlement.kotlin.service.order.model.info.CreateOrderInfo
import settlement.kotlin.service.order.model.info.ModifyOrderInfo
import settlement.kotlin.service.order.model.dto.OrderDto
import settlement.kotlin.service.order.model.dto.OrderStatus
import settlement.kotlin.service.order.model.info.QueryOrderInfo

@Service
class OrderService(
    private val ownerRepository: OwnerRepository,
    private val orderRepository: OrderRepository,
    private val orderDetailService: OrderDetailService
) {

    @Transactional
    fun createOrder(info: CreateOrderInfo): OrderDto {
        if (!ownerRepository.findById(info.ownerId).isPresent) throw RuntimeException()

        val totalPrice = info.payments.map { it.price }.sum()
        val order = orderRepository.save(
            OrderEntity(
                ownerId = info.ownerId,
                totalPrice = totalPrice,
                status = OrderStatus.ORDER_READY.name
            )
        )

        for (payment in info.payments) {
            orderDetailService.createOrderDetail(
                CreateOrderDetailInfo(
                    orderId = order.id,
                    payment = payment
                )
            )
        }

        return OrderDto(
            id = order.id,
            ownerId = order.ownerId,
            totalPrice = order.totalPrice,
            status = OrderStatus.valueOf(order.status),
            createdAt = order.createdAt
        )
    }

    @Transactional
    fun modifyOrder(info: ModifyOrderInfo): OrderDto {
        val order = orderRepository.findById(info.orderId)
            .orElseThrow(::RuntimeException)
        if (OrderStatus.valueOf(order.status) > info.orderStatus) {
            throw RuntimeException()
        }
        return orderRepository.save(order.copy(status = info.orderStatus.name)).let {
            OrderDto(
                id = it.id,
                ownerId = it.ownerId,
                totalPrice = it.totalPrice,
                status = OrderStatus.valueOf(it.status),
                createdAt = it.createdAt
            )
        }
    }

    @Transactional
    fun queryOrder(info: QueryOrderInfo, pageable: Pageable): Page<OrderDto> =
        orderRepository.queryOrder(
            orderId = info.orderId,
            ownerId = info.ownerId,
            orderStatus = info.orderStatus?.name,
            startDateTime = info.startDateTime,
            endDateTime = info.endDateTime,
            pageable = pageable
        ).map {
            OrderDto(
                id = it.id,
                ownerId = it.ownerId,
                totalPrice = it.totalPrice,
                status = OrderStatus.valueOf(it.status),
                createdAt = it.createdAt
            )
        }
}
