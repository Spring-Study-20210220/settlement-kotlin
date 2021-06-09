package settlement.kotlin.service.order.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.service.order.event.CreateOrderEvent

@Component
class OrderDetailListener(
//    private val orderDetailService: OrderDetailService
) {

    @EventListener
    @Transactional
    fun onOrderCreated(event: CreateOrderEvent) {
        event.payments.forEach {
//            orderDetailService.save()
        }
    }
}
