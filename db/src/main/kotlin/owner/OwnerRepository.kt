package settlement.kotlin.db.owner

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import settlement.kotlin.db.owner.QAccount.account

interface OwnerRepository : OwnerRepositorySupport, OwnerRepositoryJpa

interface OwnerRepositoryJpa : JpaRepository<Owner, Long> {
    fun findByEmail(email: String): Owner?
}

interface OwnerRepositorySupport {
    fun queryOwner(
        id: Long?,
        name: String?,
        email: String?,
        pageable: Pageable
    ): PageImpl<Owner>
}

@Repository
class OwnerRepositorySupportImpl : QuerydslRepositorySupport(Owner::class.java), OwnerRepositorySupport {
    override fun queryOwner(
        id: Long?,
        name: String?,
        email: String?,
        pageable: Pageable
    ): PageImpl<Owner> {
        val query = from(QOwner.owner)
            .leftJoin(QOwner.owner.accounts, account)
            .where(eqName(name), eqEmail(email), eqId(id))
            .fetchAll()

        return PageImpl<Owner>(
            querydsl!!.applyPagination(pageable, query).fetch(),
            pageable,
            query.fetchCount()
        )
    }

    fun eqName(name: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(name)) return null
        return QOwner.owner.name.eq(name)
    }

    fun eqEmail(email: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(email)) return null
        return QOwner.owner.email.eq(email)
    }

    fun eqId(id: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(id)) return null
        return QOwner.owner.id.eq(id)
    }
}
