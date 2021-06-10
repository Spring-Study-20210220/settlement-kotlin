package settlement.kotlin.db.order

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderEntity, Long> {

    fun findAll(spec: Specification<OrderEntity>, pageable: Pageable): Page<OrderEntity>
}
