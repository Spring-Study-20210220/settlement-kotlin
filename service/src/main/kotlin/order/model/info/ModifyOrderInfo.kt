package settlement.kotlin.service.order.model.info

import settlement.kotlin.service.order.model.dto.OrderStatus

data class ModifyOrderInfo(
    val orderId: Long,
    val orderStatus: OrderStatus
)
