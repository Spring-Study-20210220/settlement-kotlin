// package settlement.kotlin
//
// import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
// import com.fasterxml.jackson.module.kotlin.readValue
// import io.kotest.core.spec.style.BehaviorSpec
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.context.annotation.ComponentScan
// import org.springframework.test.web.reactive.server.WebTestClient
// import reactor.core.publisher.Mono
// import settlement.kotlin.order.res.CreateOrderResponse
// import settlement.kotlin.service.order.command.CreateOrderCommand
// import settlement.kotlin.service.order.model.dto.PaymentDto
// import settlement.kotlin.service.order.model.dto.PaymentMethod
// import settlement.kotlin.service.order.model.Price
// import settlement.kotlin.service.owner.model.info.CreateOwnerInfo
// import settlement.kotlin.service.user.model.info.RegisterUserInfo
//
// @ComponentScan(basePackages = ["settlement.kotlin"])
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class OrderControllerSpec(
//    private val webTestClient: WebTestClient
// ): BehaviorSpec() {
//
//    private val mapper = jacksonObjectMapper()
//    private val testDataSetup = TestDataSetup(webTestClient)
//
//    private val registerUserRequest = RegisterUserInfo(
//        email = "test@gmail.com",
//        password = "test1234~!",
//        nickname = "testNick",
//        isAdmin = true
//    )
//    private val createOwnerRequest = CreateOwnerInfo(
//        userId = 1L,
//        ownerName = "test",
//        ownerEmail = "test@test.test",
//        ownerPhoneNumber = "010-1111-1111"
//    )
//
//    private val createOrderRequest = CreateOrderCommand(
//        ownerId = OwnerId(value = 1L),
//        payments = listOf(PaymentDto(paymentMethod =PaymentMethod.CASH, price = Price(value = 20000)))
//    )
//
//    init {
//        beforeSpec {
//            testDataSetup.createUser(registerUserRequest)
//            testDataSetup.createOwner(createOwnerRequest)
//        }
//
//        Given("어드민 유저가 주문 생성을 요청하였다.") {
//            When("그리고 해당 업주가 존재한다."){
//                And("그러면 주문이 성공적으로 만들어진다.") {
//                    webTestClient.post().uri("/order")
//                        .body(Mono.just(createOrderRequest), CreateOrderCommand::class.java)
//                        .exchange()
//                        .expectStatus()
//                        .is2xxSuccessful
//                        .expectBody()
//                        .consumeWith {
//                            val response: SuccessResponse<CreateOrderResponse> = mapper.readValue(it.responseBody)
// //                            response.data.name shouldBe createRequest.ownerName
// //                            response.data.email shouldBe createRequest.ownerEmail
// //                            response.data.phoneNumber shouldBe createRequest.ownerPhoneNumber
//                        }
//                }
//            }
//            When("그리고 해당 업주가 존재하지 않는다.") {
//                Then("그러면 오류를 응답 받는다.") {
//
//                }
//            }
//        }
//
// //        Given("어드민 유저가 주문 수정을 요청하였다.") {
// //            When("w") {
// //            }
// //            When("") {
// //            }
// //        }
//    }
// }
