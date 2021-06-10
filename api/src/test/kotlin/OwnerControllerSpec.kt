package settlement.kotlin

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.web.reactive.server.WebTestClient

@ComponentScan(basePackages = ["settlement.kotlin"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OwnerControllerSpec(
    private val webTestClient: WebTestClient,
    private val dataCleanUp: DataCleanUp
) : BehaviorSpec() {

    init {

        Given("어드민 유저가") {
            When("업주 생성을 요청하였다.") {
                Then("중복된 이메일이 있으면 업주가 생성되지 않는다.") {
                }

                And("중복된 이메일이 없으면 업주가 생성된다.") {

                    Then("그리고 생성된 업주를 조회하였다.") {
                    }

                    Then("수정") {
                    }
                }
            }
        }
    }
}
