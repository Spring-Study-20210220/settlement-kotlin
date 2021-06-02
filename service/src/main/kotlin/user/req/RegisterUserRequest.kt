package settlement.kotlin.service.user.req

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val isAdmin: Boolean,
)
