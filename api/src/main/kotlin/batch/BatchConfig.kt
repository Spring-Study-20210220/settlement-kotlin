package settlement.kotlin.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import settlement.kotlin.batch.SettleQuery.getSettleQuery
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.order.QueryTestProjectionByConstructor
import settlement.kotlin.db.owner.OwnerEntity
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.settle.SettleEntity
import settlement.kotlin.db.settle.SettleRepository
import javax.persistence.EntityManagerFactory

@Configuration
class BatchConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
    val orderRepository: OrderRepository,
    val ownerRepository: OwnerRepository,
    val settleRepository: SettleRepository,
    val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    fun simpleJob(): Job =
        jobBuilderFactory.get("simpleJob")
            .start(simpleStep(""))
            .build()

    @Bean
    @JobScope
    fun simpleStep(@Value("#{jobParameters[date]}") dateparam: String): Step =
        stepBuilderFactory.get("simple")
            .chunk<QueryTestProjectionByConstructor, SettleEntity>(10)
            .reader(reader(dateparam))
            .processor(processor(dateparam))
            .writer(writer())
            .build()

    @Bean
    @StepScope
    fun reader(@Value("#{jobParameters[date]}") dateparam: String): ItemReader<QueryTestProjectionByConstructor> {
        val params = HashMap<String, Any>()
        params["start"] = DateTimeUtil.getStartDateTime(dateparam)
        params["end"] = DateTimeUtil.getEndDateTime(dateparam)
        return JpaPagingItemReaderBuilder<QueryTestProjectionByConstructor>()
            .name("settleReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(10)
            .queryString(getSettleQuery())
            .parameterValues(params)
            .build()
    }

    @Bean
    @StepScope
    fun processor(@Value("#{jobParameters[date]}") dateparam: String): ItemProcessor<QueryTestProjectionByConstructor, SettleEntity>{
        return ItemProcessor {
            SettleEntity(
                data = DateTimeUtil.parseDate(dateparam),
                amount = it.settleMoney,
                owner = ownerRepository.findById(it.ownerId)
                    .orElseThrow(::RuntimeException)
            )
        }
    }

    @Bean
    fun writer(): ItemWriter<SettleEntity> =
        ItemWriter { settleRepository.saveAll(it) }
}

object SettleQuery {
    fun getSettleQuery() =
        "select new settlement.kotlin.db.order.QueryTestProjectionByConstructor(" +
                "o.owner_id as ownerId, " +
                "sum(o.total_price) as settleMoney " +
                ") " +
                "from orders as o " +
                "group by o.owner_id " +
                "order by o.id "
}