// package settlement.kotlin
//
// import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
// import com.fasterxml.jackson.module.kotlin.readValue
// import io.kotest.core.spec.style.BehaviorSpec
// import io.kotest.matchers.shouldBe
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.context.annotation.ComponentScan
// import org.springframework.test.web.reactive.server.WebTestClient
// import reactor.core.publisher.Mono
// import settlement.kotlin.service.owner.model.info.CreateOwnerInfo
// import settlement.kotlin.service.owner.model.info.UpdateOwnerInfo
// import settlement.kotlin.service.user.model.info.RegisterUserInfo
//
// @ComponentScan(basePackages = ["settlement.kotlin"])
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class OwnerControllerSpec(
//    private val webTestClient: WebTestClient
// ) : BehaviorSpec() {
//
//    private val mapper = jacksonObjectMapper()
//    private val testDataSetup = TestDataSetup(webTestClient)
//
//    val createRequest = CreateOwnerInfo(
//        userId = 1L,
//        ownerName = "test",
//        ownerEmail = "test@test.test",
//        ownerPhoneNumber = "010-1111-1111"
//    )
//    val updateRequest = UpdateOwnerInfo(ownerId = 1L, name = "updateName", phoneNumber = "010-2222-3333")
//
//    init {
//        beforeSpec {
//            testDataSetup.createAdminUser(
//                req = RegisterUserInfo(
//                    email = "testUser@test.com",
//                    password = "test1234~!",
//                    nickname = "testNickname",
//                    isAdmin = true
//                )
//            )
//        }
//
//        Given("어드민 유저가") {
//            When("업주 생성을 요청하였다.") {
//                And("중복된 이메일이 없으면 업주가 생성된다.") {
//                    webTestClient.post().uri("/owner")
//                        .body(Mono.just(createRequest), CreateOwnerInfo::class.java)
//                        .exchange()
//                        .expectStatus()
//                        .is2xxSuccessful
//                        .expectBody()
//                        .consumeWith {
//                            val response: SuccessResponse<CreateOwnerResponse> = mapper.readValue(it.responseBody)
//
//                            response.data.name shouldBe createRequest.ownerName
//                            response.data.email shouldBe createRequest.ownerEmail
//                            response.data.phoneNumber shouldBe createRequest.ownerPhoneNumber
//                        }
//                    Then("그리고 생성된 업주를 조회하였다.") {
//                        webTestClient.get()
//                            .uri {
//                                it.path("/owner/list")
//                                    .queryParam("page", 0)
//                                    .queryParam("size", 20)
//                                    .queryParam("ownerId", 1L)
//                                    .build()
//                            }
//                            .exchange()
//                            .expectStatus()
//                            .is2xxSuccessful
//                            .expectBody()
//                            .consumeWith {
//                                val response: PageResponse<OwnerResponse> = mapper.readValue(it.responseBody)
//
//                                response.list.size shouldBe 1
//                                response.list[0].id shouldBe 1L
//                            }
//                    }
//                    Then("그리고 생성된 업주 정보를 수정하였다.") {
//                        webTestClient.put().uri("/owner")
//                            .body(Mono.just(updateRequest), UpdateOwnerInfo::class.java)
//                            .exchange()
//                            .expectStatus()
//                            .is2xxSuccessful
//                            .expectBody()
//                            .consumeWith {
//                                val response: SuccessResponse<UpdateOwnerResponse> = mapper.readValue(it.responseBody)
//
//                                response.data.name shouldBe updateRequest.name
//                                response.data.phoneNumber shouldBe updateRequest.phoneNumber
//                            }
//                    }
//                }
//
//                Then("중복된 이메일이 있으면 업주가 생성되지 않는다.") {
//                    webTestClient.post().uri("/owner")
//                        .body(Mono.just(createRequest), CreateOwnerInfo::class.java)
//                        .exchange()
//                        .expectStatus()
//                        .is5xxServerError
//                }
//            }
//        }
//    }
// }
