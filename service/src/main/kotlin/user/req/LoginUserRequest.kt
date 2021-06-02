package settlement.kotlin.service.user.req

data class LoginUserRequest(
    val email: String,
    val password: String
)
