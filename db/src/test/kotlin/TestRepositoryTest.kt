package settlment.kotlin

import io.kotest.core.spec.style.FeatureSpec
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import settlement.kotlin.db.QueryDslConfiguration
import settlement.kotlin.db.TestRepository
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository

@EnableJpaRepositories(basePackages = ["settlement.kotlin"])
@EntityScan(basePackages = ["settlement.kotlin"])
@Import(QueryDslConfiguration::class)
@ComponentScan(basePackages = ["settlement.kotlin"])
@DataJpaTest
class TestRepositoryTest(
    private val testRepository: TestRepository,
    private val ownerRepository: OwnerRepository
) : FeatureSpec() {

    init {
        feature("a") {
            scenario("b") {
                ownerRepository.save(
                    Owner(
                        id = 0, name = "a", email = "bb", phoneNumber = "c"
                    )
                )
                ownerRepository.save(
                    Owner(
                        id = 0, name = "a", email = "bb", phoneNumber = "c"
                    )
                )
                ownerRepository.save(
                    Owner(
                        id = 0, name = "a", email = "bbb", phoneNumber = "c"
                    )
                )

                testRepository.findMyOrder(
                    id = null,
                    name = "a",
                    email = "bb",
                    offset = 0,
                    size = 1
                ).also(::println)

                testRepository.findMyOrder(
                    id = null,
                    name = "a",
                    email = "bb",
                    offset = 0,
                    size = 2
                ).also(::println)
            }
        }
    }
}
