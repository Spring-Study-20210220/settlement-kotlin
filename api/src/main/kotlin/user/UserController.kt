package settlement.kotlin.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import settlement.kotlin.SuccessResponse
import settlement.kotlin.service.user.UserService
import settlement.kotlin.service.user.model.info.LoginUserInfo
import settlement.kotlin.service.user.model.info.RegisterUserInfo

@RequestMapping("user")
@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("")
    fun register(@RequestBody req: RegisterUserInfo) =
        SuccessResponse(data = userService.register(req))

    @PostMapping("/login")
    fun login(@RequestBody req: LoginUserInfo) =
        SuccessResponse(data = userService.login(req))
}
