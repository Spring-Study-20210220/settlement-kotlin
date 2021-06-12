package settlement.kotlin

import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import settlement.kotlin.service.user.req.RegisterUserRequest

class TestDataSetup(
    private val webTestClient: WebTestClient,
) {

    fun createAdminUser(req: RegisterUserRequest) =
        webTestClient.post().uri("/user")
            .body(Mono.just(req), RegisterUserRequest::class.java)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
}
