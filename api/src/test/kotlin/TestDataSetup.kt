// package settlement.kotlin
//
// import org.springframework.test.web.reactive.server.WebTestClient
// import reactor.core.publisher.Mono
// import settlement.kotlin.service.user.model.info.RegisterUserInfo
//
// class TestDataSetup(
//    private val webTestClient: WebTestClient,
// ) {
//
//    fun createAdminUser(req: RegisterUserInfo) =
//        webTestClient.post().uri("/user")
//            .body(Mono.just(req), RegisterUserInfo::class.java)
//            .exchange()
//            .expectStatus()
//            .is2xxSuccessful
// }
