package settlement.kotlin.db.settle

import settlement.kotlin.db.owner.OwnerEntity
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "settles")
data class SettleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val data: LocalDate,
    val amount: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    val owner: OwnerEntity
)