// package settlement.kotlin
//
// import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
// import com.fasterxml.jackson.module.kotlin.readValue
// import io.kotest.core.spec.style.FeatureSpec
// import io.kotest.matchers.shouldBe
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.context.annotation.ComponentScan
// import org.springframework.test.web.reactive.server.WebTestClient
// import reactor.core.publisher.Mono
// import settlement.kotlin.service.user.req.LoginUserRequest
// import settlement.kotlin.service.user.req.RegisterUserRequest
// import settlement.kotlin.service.user.res.LoginUserResponse
//
// @ComponentScan(basePackages = ["settlement.kotlin"])
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class UserControllerSpec(
//    private val webTestClient: WebTestClient,
//    private val dataCleanUp: DataCleanUp
// ) : FeatureSpec() {
//
//    private val mapper = jacksonObjectMapper()
//
//    init {
//        afterEach {
//            dataCleanUp.execute()
//        }
//
//        feature("회원 가입 기능") {
//            scenario("성공적으로 가입하였다.") {
//                val req = RegisterUserRequest(
//                    email = "test@gmail.com",
//                    password = "test1234~!",
//                    nickname = "testNick",
//                    isAdmin = false
//                )
//
//                webTestClient.post().uri("/user")
//                    .body(Mono.just(req), RegisterUserRequest::class.java)
//                    .exchange()
//                    .expectStatus()
//                    .is2xxSuccessful
//                    .expectBody()
//                    .consumeWith {
//                        val response: SuccessResponse<RegisterUserResponse> = mapper.readValue(it.responseBody)
//
//                        response.data.email shouldBe req.email
//                        response.data.nickname shouldBe req.nickname
//                        response.data.isAdmin shouldBe req.isAdmin
//                    }
//            }
//
//            scenario("회원 이메일이 중복되었다.") {
//                val req = RegisterUserRequest(
//                    email = "test@gmail.com",
//                    password = "test1234~!",
//                    nickname = "testNick",
//                    isAdmin = false
//                )
//
//                webTestClient.post().uri("/user")
//                    .body(Mono.just(req), RegisterUserRequest::class.java)
//                    .exchange()
//                    .expectStatus()
//                    .is2xxSuccessful
//                    .expectBody()
//                    .consumeWith {
//                        val response: SuccessResponse<RegisterUserResponse> = mapper.readValue(it.responseBody)
//
//                        response.data.email shouldBe req.email
//                        response.data.nickname shouldBe req.nickname
//                        response.data.isAdmin shouldBe req.isAdmin
//                    }
//
//                webTestClient.post().uri("/user")
//                    .body(Mono.just(req), RegisterUserRequest::class.java)
//                    .exchange()
//                    .expectStatus()
//                    .is5xxServerError
//            }
//        }
//
//        feature("회원 로그인 기능") {
//            val registerUserRequest = RegisterUserRequest(
//                email = "test@gmail.com",
//                password = "test1234~!",
//                nickname = "testNick",
//                isAdmin = false
//            )
//
//            webTestClient.post().uri("/user")
//                .body(Mono.just(registerUserRequest), RegisterUserRequest::class.java)
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful
//
//            scenario("성공적으로 로그인 하였다.") {
//                val loginUserRequest = LoginUserRequest(
//                    email = "test@gmail.com",
//                    password = "test1234~!"
//                )
//
//                webTestClient.post().uri("/user/login")
//                    .body(Mono.just(loginUserRequest), LoginUserRequest::class.java)
//                    .exchange()
//                    .expectStatus()
//                    .is2xxSuccessful
//                    .expectBody()
//                    .consumeWith {
//                        val response: SuccessResponse<LoginUserResponse> = mapper.readValue(it.responseBody)
//                        response.data.isComplete shouldBe true
//                    }
//            }
//
//            scenario("이메일이 존재하지 않는다.") {
//                val loginUserRequest = LoginUserRequest(
//                    email = "test404@gmail.com",
//                    password = "test1234~!"
//                )
//
//                webTestClient.post().uri("/user/login")
//                    .body(Mono.just(loginUserRequest), LoginUserRequest::class.java)
//                    .exchange()
//                    .expectStatus()
//                    .is2xxSuccessful
//                    .expectBody()
//                    .consumeWith {
//                        val response: SuccessResponse<LoginUserResponse> = mapper.readValue(it.responseBody)
//                        response.data.isComplete shouldBe false
//                    }
//            }
//
//            scenario("패스워드가 잘못되었다.") {
//                val loginUserRequest = LoginUserRequest(
//                    email = "test@gmail.com",
//                    password = "test404~!"
//                )
//
//                webTestClient.post().uri("/user/login")
//                    .body(Mono.just(loginUserRequest), LoginUserRequest::class.java)
//                    .exchange()
//                    .expectStatus()
//                    .is2xxSuccessful
//                    .expectBody()
//                    .consumeWith {
//                        val response: SuccessResponse<LoginUserResponse> = mapper.readValue(it.responseBody)
//                        response.data.isComplete shouldBe false
//                    }
//            }
//        }
//    }
// }
