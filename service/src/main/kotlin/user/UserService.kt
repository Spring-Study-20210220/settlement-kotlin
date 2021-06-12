package settlement.kotlin.service.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.user.UserEntity
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.user.model.info.LoginUserInfo
import settlement.kotlin.service.user.model.info.RegisterUserInfo
import settlement.kotlin.service.user.model.dto.UserDto

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun register(req: RegisterUserInfo): UserDto =
        userRepository.findByEmail(req.email)?.let {
            throw RuntimeException()
        } ?: userRepository.save(
            UserEntity(
                email = req.email,
                password = req.password,
                nickname = req.nickname,
                isAdmin = req.isAdmin
            )
        ).let {
            UserDto(
                id = it.id,
                email = it.email,
                nickname = it.nickname,
                isAdmin = it.isAdmin
            )
        }

    fun login(req: LoginUserInfo): UserDto {
        val user = userRepository.findByEmail(req.email)
        if (user == null || user.password != req.password) {
            throw RuntimeException()
        }
        return user.let {
            UserDto(
                id = it.id,
                email = it.email,
                nickname = it.nickname,
                isAdmin = it.isAdmin
            )
        }
    }
}
