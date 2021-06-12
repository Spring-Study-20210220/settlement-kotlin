package settlement.kotlin.owner

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import settlement.kotlin.PageResponse
import settlement.kotlin.SuccessResponse
import settlement.kotlin.service.owner.OwnerService
import settlement.kotlin.service.owner.req.CreateOwnerRequest
import settlement.kotlin.service.owner.req.QueryOwnerRequest
import settlement.kotlin.service.owner.req.UpdateOwnerRequest
import settlement.kotlin.service.owner.res.CreateOwnerResponse
import settlement.kotlin.service.owner.res.OwnerResponse
import settlement.kotlin.service.owner.res.UpdateOwnerResponse

@RequestMapping("/owner")
@RestController
class OwnerController(
    private val ownerService: OwnerService
) {

    @PostMapping
    fun createOwner(@RequestBody createOwnerRequest: CreateOwnerRequest): SuccessResponse<CreateOwnerResponse> =
        SuccessResponse(
            data = ownerService.createOwner(createOwnerRequest)
        )

    @PutMapping
    fun updateOwner(@RequestBody updateOwnerRequest: UpdateOwnerRequest): SuccessResponse<UpdateOwnerResponse> =
        SuccessResponse(
            data = ownerService.updateOwner(updateOwnerRequest)
        )

    @GetMapping("/list")
    fun queryOwner(queryOwnerRequest: QueryOwnerRequest, pageable: Pageable): PageResponse<OwnerResponse> =
        ownerService.queryOwner(queryOwnerRequest, pageable).let {
            PageResponse(
                list = it.content,
                size = it.content.size,
                offset = pageable.offset.toInt(),
                totalCount = it.totalElements.toInt(),
                hasNext = it.hasNext()
            )
        }
}
