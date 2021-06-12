package settlement.kotlin.service.user.model.dto

data class UserDto(
    val id: Long,
    val email: String,
    val nickname: String,
    val isAdmin: Boolean
)
