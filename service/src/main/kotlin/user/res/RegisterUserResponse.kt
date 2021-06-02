package settlement.kotlin.service.user.res

data class RegisterUserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val isAdmin: Boolean,
)
