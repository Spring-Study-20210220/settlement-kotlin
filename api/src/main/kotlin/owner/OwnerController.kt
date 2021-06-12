// package settlement.kotlin.owner
//
// import org.springframework.data.domain.Pageable
// import org.springframework.web.bind.annotation.GetMapping
// import org.springframework.web.bind.annotation.PostMapping
// import org.springframework.web.bind.annotation.PutMapping
// import org.springframework.web.bind.annotation.RequestBody
// import org.springframework.web.bind.annotation.RequestMapping
// import org.springframework.web.bind.annotation.RestController
// import settlement.kotlin.PageResponse
// import settlement.kotlin.SuccessResponse
// import settlement.kotlin.service.owner.OwnerService
// import settlement.kotlin.service.owner.model.info.CreateOwnerInfo
// import settlement.kotlin.service.owner.model.info.QueryOwnerInfo
// import settlement.kotlin.service.owner.model.info.UpdateOwnerInfo
//
// @RequestMapping("/owner")
// @RestController
// class OwnerController(
//    private val ownerService: OwnerService
// ) {
//
//    @PostMapping
//    fun createOwner(@RequestBody createOwnerInfo: CreateOwnerInfo): SuccessResponse<CreateOwnerResponse> =
//        SuccessResponse(
//            data = ownerService.createOwner(createOwnerInfo)
//        )
//
//    @PutMapping
//    fun updateOwner(@RequestBody updateOwnerInfo: UpdateOwnerInfo): SuccessResponse<UpdateOwnerResponse> =
//        SuccessResponse(
//            data = ownerService.updateOwner(updateOwnerInfo)
//        )
//
//    @GetMapping("/list")
//    fun queryOwner(queryOwnerInfo: QueryOwnerInfo, pageable: Pageable): PageResponse<OwnerResponse> =
//        ownerService.queryOwner(queryOwnerInfo, pageable).let {
//            PageResponse(
//                list = it.content,
//                size = it.content.size,
//                offset = pageable.offset.toInt(),
//                totalCount = it.totalElements.toInt(),
//                hasNext = it.hasNext()
//            )
//        }
// }
