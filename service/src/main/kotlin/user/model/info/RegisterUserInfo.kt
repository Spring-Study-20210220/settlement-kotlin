package settlement.kotlin.service.user.model.info

data class RegisterUserInfo(
    val email: String,
    val password: String,
    val nickname: String,
    val isAdmin: Boolean,
)
