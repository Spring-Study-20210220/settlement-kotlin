package settlement.kotlin.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import settlement.kotlin.service.user.UserService

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("")
    fun save() {
    }
}
