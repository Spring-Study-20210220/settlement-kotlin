package settlement.kotlin.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.user.UserService
import settlement.kotlin.service.user.model.info.RegisterUserInfo

@Configuration
class BatchConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
    val orderRepository: OrderRepository
) {

    @Bean
    fun simpleJob(): Job =
        jobBuilderFactory.get("simpleJob")
            .start(simpleStep())
            .build()

    @Bean
    fun simpleStep(): Step =
        stepBuilderFactory.get("simple")
            .tasklet { stepContribution, chunkContext ->
                null
            }
            .build()

    @Bean
    fun customItemReader(): RepositoryItemReader<OrderEntity> =
            RepositoryItemReaderBuilder<OrderEntity>()
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(10)
                .maxItemCount(10)
                .

}