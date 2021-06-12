package settlement.kotlin.db.owner

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity(name = "owners")
data class OwnerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val email: String,
    val phoneNumber: String,
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    val accounts: List<AccountEntity>
)
