package settlement.kotlin.service.owner

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import settlement.kotlin.db.owner.OwnerEntity
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.db.user.UserRepository
import settlement.kotlin.service.owner.model.info.CreateOwnerInfo
import settlement.kotlin.service.owner.model.dto.OwnerDto
import settlement.kotlin.service.owner.model.info.QueryOwnerInfo
import settlement.kotlin.service.owner.model.info.UpdateOwnerInfo

@Service
class OwnerService(
    private val ownerRepository: OwnerRepository,
    private val userRepository: UserRepository
) {

    fun createOwner(createOwnerInfo: CreateOwnerInfo): OwnerDto {
        userRepository.findByIdAndIsAdmin(createOwnerInfo.userId, true)
            ?: throw RuntimeException("권한이 없는 요청입니다.")

        ownerRepository.findByEmail(createOwnerInfo.ownerEmail)?.let {
            throw RuntimeException("중복된 이메일입니다.")
        }

        return ownerRepository.save(
            OwnerEntity(
                name = createOwnerInfo.ownerName,
                email = createOwnerInfo.ownerEmail,
                phoneNumber = createOwnerInfo.ownerPhoneNumber,
                accounts = emptyList()
            )
        ).run {
            OwnerDto(
                id = id,
                name = name,
                email = email,
                phoneNumber = phoneNumber
            )
        }
    }

    fun queryOwner(req: QueryOwnerInfo, pageable: Pageable): Page<OwnerDto> =
        ownerRepository.queryOwner(
            id = req.ownerId,
            name = req.ownerName,
            email = req.ownerEmail,
            pageable = pageable
        ).map {
            OwnerDto(
                id = it.id,
                name = it.name,
                email = it.email,
                phoneNumber = it.phoneNumber
            )
        }

    fun updateOwner(req: UpdateOwnerInfo): OwnerDto {
        val owner = ownerRepository.findById(req.ownerId).orElseThrow(::RuntimeException)

        return ownerRepository.save(
            owner.copy(
                name = req.name ?: owner.name,
                phoneNumber = req.phoneNumber ?: owner.phoneNumber
            )
        ).let {
            OwnerDto(
                id = it.id,
                name = it.name,
                email = it.email,
                phoneNumber = it.phoneNumber
            )
        }
    }
}
