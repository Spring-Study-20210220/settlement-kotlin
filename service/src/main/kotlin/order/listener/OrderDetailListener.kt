package settlement.kotlin.service.order.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.service.order.event.CreateOrderEvent

@Component
class OrderDetailListener {

    @EventListener
    @Transactional
    fun onOrderCreated(event: CreateOrderEvent) {
        println("Order Created!")
    }
}