package settlement.kotlin.service.owner

import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.owner.req.CreateOwnerRequest
import settlement.kotlin.service.owner.res.CreateOwnerResponse

class OwnerService(
    private val ownerRepository: OwnerRepository,
    private val userRepository: UserRepository
) {

    fun createOwner(createOwnerRequest: CreateOwnerRequest): CreateOwnerResponse {
        userRepository.findByIdAndIsAdmin(createOwnerRequest.userId, true)
            ?: throw RuntimeException("권한이 없는 요청입니다.")

        ownerRepository.findByEmail(createOwnerRequest.ownerEmail)?.let {
            throw RuntimeException("중복된 이메일입니다.")
        }

        return ownerRepository.save(
            Owner(
                name = createOwnerRequest.ownerName,
                email = createOwnerRequest.ownerEmail,
                phoneNumber = createOwnerRequest.ownerPhoneNumber
            )
        ).run {
            CreateOwnerResponse(
                id = id,
                name = name,
                email = email,
                phoneNumber = phoneNumber
            )
        }
    }
}
