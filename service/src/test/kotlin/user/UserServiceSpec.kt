package settlement.kotlin.service.user

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import settlement.kotlin.db.user.UserEntity
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.user.model.info.LoginUserInfo
import settlement.kotlin.service.user.model.info.RegisterUserInfo

class UserServiceSpec : FeatureSpec() {

    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    private val userRepository: UserRepository = mockk()
    private val userService: UserService = UserService(userRepository)
    private val emailSlot = slot<String>()

    init {
        feature("유저 회원 가입") {
            every { userRepository.findByEmail(capture(emailSlot)) } answers {
                if (emailSlot.captured == duplicatedRegisterUserRequest.email) {
                    throw RuntimeException()
                }
                null
            }

            every { userRepository.save(any()) } returns UserEntity(
                id = 1,
                email = registerUserRequest.email,
                password = registerUserRequest.password,
                nickname = registerUserRequest.nickname,
                isAdmin = registerUserRequest.isAdmin
            )

            scenario("중복된 이메일이 존재한다") {
                shouldThrowExactly<RuntimeException> {
                    userService.register(duplicatedRegisterUserRequest)
                }
            }

            scenario("정상적으로 가입되었다") {
                val registerUserResponse = userService.register(registerUserRequest)

                registerUserResponse.email shouldBe "testuser@gmail.com"
                registerUserResponse.nickname shouldBe "testuser"
                registerUserResponse.isAdmin shouldBe false
            }
        }

        feature("유저 로그인 기능") {
            every { userRepository.findByEmail(any()) } returns UserEntity(
                id = 1,
                email = "testuser@gmail.com",
                password = "1234",
                nickname = "test",
                isAdmin = false
            )

            scenario("이메일과 비밀번호가 일치한다.") {
                val userDto = userService.login(loginSucceedRequest)

                userDto.email shouldBe "testuser@gmail.com"
                userDto.nickname shouldBe "test"
            }

            scenario("이메일과 비밀번호가 일치하지 않는다.") {
                shouldThrowExactly<RuntimeException> {
                    userService.login(loginFailRequest)
                }
            }
        }
    }

    private val registerUserRequest = RegisterUserInfo(
        email = "testuser@gmail.com",
        password = "pass1234~!",
        nickname = "testuser",
        isAdmin = false
    )

    private val duplicatedRegisterUserRequest = RegisterUserInfo(
        email = "duplicate@gmail.com",
        password = "pass1234~!",
        nickname = "testuser",
        isAdmin = false
    )

    private val loginSucceedRequest = LoginUserInfo(
        email = "testuser@gmail.com", password = "1234"
    )

    private val loginFailRequest = LoginUserInfo(
        email = "testuser@gmail.com", password = "123"
    )
}
