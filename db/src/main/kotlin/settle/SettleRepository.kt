package settlement.kotlin.db.settle

import org.springframework.data.jpa.repository.JpaRepository

interface SettleRepository : JpaRepository<SettleEntity, Long>