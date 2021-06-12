package settlement.kotlin.db.owner

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import settlement.kotlin.db.owner.QAccountEntity.accountEntity

interface OwnerRepository : OwnerRepositorySupport, OwnerRepositoryJpa

interface OwnerRepositoryJpa : JpaRepository<OwnerEntity, Long> {
    fun findByEmail(email: String): OwnerEntity?
}

interface OwnerRepositorySupport {
    fun queryOwner(
        id: Long?,
        name: String?,
        email: String?,
        pageable: Pageable
    ): PageImpl<OwnerEntity>
}

@Repository
class OwnerRepositorySupportImpl : QuerydslRepositorySupport(OwnerEntity::class.java), OwnerRepositorySupport {
    override fun queryOwner(
        id: Long?,
        name: String?,
        email: String?,
        pageable: Pageable
    ): PageImpl<OwnerEntity> {
        val query = from(QOwnerEntity.ownerEntity)
            .leftJoin(QOwnerEntity.ownerEntity.accounts, accountEntity)
            .where(eqName(name), eqEmail(email), eqId(id))
            .fetchAll()

        return PageImpl<OwnerEntity>(
            querydsl!!.applyPagination(pageable, query).fetch(),
            pageable,
            query.fetchCount()
        )
    }

    fun eqName(name: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(name)) return null
        return QOwnerEntity.ownerEntity.name.eq(name)
    }

    fun eqEmail(email: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(email)) return null
        return QOwnerEntity.ownerEntity.email.eq(email)
    }

    fun eqId(id: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(id)) return null
        return QOwnerEntity.ownerEntity.id.eq(id)
    }
}
