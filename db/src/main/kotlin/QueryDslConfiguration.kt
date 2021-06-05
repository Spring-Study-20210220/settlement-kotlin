package settlement.kotlin.db

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.QOwner.owner
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class QueryDslConfiguration(
    @PersistenceContext val entityManager: EntityManager
) {
    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}

@Repository
class TestRepository : QuerydslRepositorySupport(Owner::class.java) {
    fun findMyOrder(
        id: Long?,
        name: String?,
        email: String?,
        offset: Long,
        size: Long,
    ): List<Owner> =
        from(owner).where(eqName(name), eqEmail(email), eqId(id))
            .offset(offset)
            .limit(size)
            .fetch()

    fun eqName(name: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(name)) return null
        return owner.name.eq(name)
    }

    fun eqEmail(email: String?): BooleanExpression? {
        if (ObjectUtils.isEmpty(email)) return null
        return owner.email.eq(email)
    }

    fun eqId(id: Long?): BooleanExpression? {
        if (ObjectUtils.isEmpty(id)) return null
        return owner.id.eq(id)
    }
}
