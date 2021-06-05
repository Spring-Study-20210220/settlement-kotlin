package settlement.kotlin.service.owner

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.springframework.data.domain.PageRequest
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.user.User
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.owner.req.CreateOwnerRequest
import settlement.kotlin.service.owner.req.QueryOwnerRequest

class OwnerServiceSpec : FeatureSpec() {
    private val ownerRepository: OwnerRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val ownerService = OwnerService(ownerRepository, userRepository)

    private val userIdSlot = slot<Long>()
    private val emailSlot = slot<String>()
    private val adminUser = User(id = 1L, email = "test", password = "test", nickname = "test", isAdmin = true)
    private val owner = Owner(id = 1L, name = "test", email = "test@test.test", phoneNumber = "010-1111-1111")

    init {
        every {
            userRepository.findByIdAndIsAdmin(capture(userIdSlot), any())
        }.answers {
            if (userIdSlot.captured == 1L) adminUser
            else null
        }

        every { ownerRepository.findByEmail(capture(emailSlot)) }.answers {
            if (emailSlot.captured == "duplicate@duplicate.duplicate") owner
            else null
        }

        every { ownerRepository.save(any()) } returns owner

        feature("업주 등록 기능") {
            scenario("중복된 이메일이 없으면, 정상적으로 등록한다.") {
                val req = CreateOwnerRequest(
                    userId = 1L,
                    ownerName = "test",
                    ownerEmail = "test@test.test",
                    ownerPhoneNumber = "010-1111-1111"
                )

                val result = ownerService.createOwner(req)

                result.name shouldBe req.ownerName
                result.email shouldBe req.ownerEmail
                result.phoneNumber shouldBe req.ownerPhoneNumber
            }

            scenario("중복된 이메일이 있으면, 예외가 발생한다.") {
                val req = CreateOwnerRequest(
                    userId = 1L,
                    ownerName = "test",
                    ownerEmail = "duplicate@duplicate.duplicate",
                    ownerPhoneNumber = "010-1111-1111"
                )

                shouldThrowExactly<RuntimeException> { ownerService.createOwner(req) }
            }

            scenario("권한이 없는 유저 요청의 경우, 예외가 발생한다.") {
                val req = CreateOwnerRequest(
                    userId = 10L,
                    ownerName = "test",
                    ownerEmail = "test@test.test",
                    ownerPhoneNumber = "010-1111-1111"
                )

                shouldThrowExactly<RuntimeException> { ownerService.createOwner(req) }
            }
        }

        feature("업주 조회 기능") {
            scenario("업주 고유 번호, 업주 이름, 업주 이메일에 따라 업주를 조회한다.") {
                val req = QueryOwnerRequest(
                    ownerId = 1L,
                    ownerName = "test",
                    ownerEmail = "test@test.test"
                )

                val pageable = PageRequest.of(0, 3)

                val result = ownerService.queryOwner(req, pageable)

                result.content
            }
        }
    }
}
