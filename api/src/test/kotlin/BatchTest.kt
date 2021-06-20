import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import settlement.kotlin.batch.BatchConfig
import settlement.kotlin.db.order.OrderEntity
import settlement.kotlin.db.order.OrderRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.settle.SettleRepository

@SpringBootTest(classes = [TestBatchConfig::class,BatchConfig::class])
@SpringBatchTest
@EntityScan(basePackages = ["settlement.kotlin"])
@EnableJpaRepositories(basePackages = ["settlement.kotlin"])
@ComponentScan(basePackages = ["settlement.kotlin"])
class BatchTest(
    val jobLauncherTestUtils: JobLauncherTestUtils,
    val settleRepository: SettleRepository,
    val orderRepository: OrderRepository,
    val ownerRepository: OwnerRepository
): FeatureSpec() {

    init {

        afterSpec {
            settleRepository.deleteAll()
        }

        beforeTest {
            ownerRepository.saveAll(BatchTestData.getTestOwners())
            orderRepository.saveAll(BatchTestData.getTestOrders())
        }


        feature("정산 배치"){
            scenario("정상 작동"){

                val jobParameters = JobParametersBuilder()
                    .addString("date","2021-06-10")
                    .toJobParameters()

                val jobExecution = jobLauncherTestUtils.launchJob(jobParameters)
                val result = settleRepository.findAll()

                jobExecution.status shouldBe BatchStatus.COMPLETED
                result.size shouldBe 5
            }
        }
    }
}