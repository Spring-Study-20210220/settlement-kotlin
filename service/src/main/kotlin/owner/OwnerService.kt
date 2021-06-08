package settlement.kotlin.service.owner

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.owner.req.CreateOwnerRequest
import settlement.kotlin.service.owner.req.ModifyOwnerRequest
import settlement.kotlin.service.owner.req.QueryOwnerRequest
import settlement.kotlin.service.owner.res.CreateOwnerResponse
import settlement.kotlin.service.owner.res.ModifyOwnerResponse
import settlement.kotlin.service.owner.res.OwnerResponse

class OwnerService(
    private val ownerRepository: OwnerRepository,
    private val userRepository: UserRepository
) {

    fun createOwner(createOwnerRequest: CreateOwnerRequest): OwnerResponse {
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
            OwnerResponse(
                id = id,
                name = name,
                email = email,
                phoneNumber = phoneNumber
            )
        }
    }

    fun queryOwner(req: QueryOwnerRequest, pageable: Pageable): Page<OwnerResponse> =
        ownerRepository.queryOwner(
            id = req.ownerId,
            name = req.ownerName,
            email = req.ownerEmail,
            pageable = pageable
        ).map {
            OwnerResponse(
                id = it.id,
                name = it.name,
                email = it.email,
                phoneNumber = it.phoneNumber
            )
        }

    fun modifyOwner(req: ModifyOwnerRequest, ownerId: Long): ModifyOwnerResponse {
        val owner = ownerRepository.findById(ownerId)
            .orElseThrow(::RuntimeException)
        owner.modifyOwnerInfo(name = req.ownerName, phoneNumber = req.ownerPhoneNumber)
        return ModifyOwnerResponse(
            id = owner.id,
            name = owner.name,
            email = owner.email,
            phoneNumber = owner.phoneNumber
        )
    }
}
