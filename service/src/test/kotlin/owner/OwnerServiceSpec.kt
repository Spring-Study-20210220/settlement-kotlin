package settlement.kotlin.service.owner

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import settlement.kotlin.db.owner.OwnerEntity
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.user.UserEntity
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.owner.model.info.CreateOwnerInfo
import settlement.kotlin.service.owner.model.info.UpdateOwnerInfo
import java.util.Optional

class OwnerServiceSpec : FeatureSpec() {
    private val ownerRepository: OwnerRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val ownerService = OwnerService(ownerRepository, userRepository)

    private val userIdSlot = slot<Long>()
    private val emailSlot = slot<String>()
    private val adminUser = UserEntity(id = 1L, email = "test", password = "test", nickname = "test", isAdmin = true)

    private val ownerIdSlot = slot<Long>()
    private val ownerSlot = slot<OwnerEntity>()
    private val owner = OwnerEntity(id = 1L, name = "test", email = "test@test.test", phoneNumber = "010-1111-1111", emptyList())

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

        every { ownerRepository.save(capture(ownerSlot)) }.answers {
            val capturedOwner = ownerSlot.captured
            if (capturedOwner.id == 0L) {
                capturedOwner.copy(id = 1L)
            } else {
                capturedOwner
            }
        }

        every { ownerRepository.findById(capture(ownerIdSlot)) }.answers {
            if (ownerIdSlot.captured == owner.id) Optional.of(owner)
            else Optional.empty()
        }

        feature("?????? ?????? ??????") {
            scenario("????????? ???????????? ?????????, ??????????????? ????????????.") {
                val req = CreateOwnerInfo(
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

            scenario("????????? ???????????? ?????????, ????????? ????????????.") {
                val req = CreateOwnerInfo(
                    userId = 1L,
                    ownerName = "test",
                    ownerEmail = "duplicate@duplicate.duplicate",
                    ownerPhoneNumber = "010-1111-1111"
                )

                shouldThrowExactly<RuntimeException> { ownerService.createOwner(req) }
            }

            scenario("????????? ?????? ?????? ????????? ??????, ????????? ????????????x.") {
                val req = CreateOwnerInfo(
                    userId = 10L,
                    ownerName = "test",
                    ownerEmail = "test@test.test",
                    ownerPhoneNumber = "010-1111-1111"
                )

                shouldThrowExactly<RuntimeException> { ownerService.createOwner(req) }
            }
        }

        feature("?????? ???????????? ??????") {
            scenario("????????? ???????????? ?????????, ????????? ????????????.") {
                shouldThrowExactly<RuntimeException> {
                    ownerService.updateOwner(
                        UpdateOwnerInfo(ownerId = 100, name = "test", phoneNumber = "test")
                    )
                }
            }

            scenario("????????? ???????????????, ????????? ?????? ?????? ????????? ????????????.") {
                val result = ownerService.updateOwner(
                    UpdateOwnerInfo(ownerId = 1, name = "jon", phoneNumber = "1111-1111")
                )

                result.id shouldBe 1L
                result.name shouldBe "jon"
                result.phoneNumber shouldBe "1111-1111"
            }
        }
    }
}
