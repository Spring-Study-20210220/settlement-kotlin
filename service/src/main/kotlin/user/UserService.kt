package settlement.kotlin.service.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.user.User
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.user.req.LoginUserRequest
import settlement.kotlin.service.user.req.RegisterUserRequest
import settlement.kotlin.service.user.res.LoginUserResponse
import settlement.kotlin.service.user.res.RegisterUserResponse

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun register(req: RegisterUserRequest): RegisterUserResponse =
        userRepository.findByEmail(req.email)?.let {
            throw RuntimeException()
        } ?: userRepository.save(
            User(
                email = req.email,
                password = req.password,
                nickname = req.nickname,
                isAdmin = req.isAdmin
            )
        ).let {
            RegisterUserResponse(
                id = it.id,
                email = it.email,
                nickname = it.nickname,
                isAdmin = it.isAdmin
            )
        }

    fun login(req: LoginUserRequest): LoginUserResponse {
        val user = userRepository.findByEmail(req.email)
        if (user == null || user.password != req.password) {
            return LoginUserResponse(false)
        }
        return LoginUserResponse(true)
    }
}
