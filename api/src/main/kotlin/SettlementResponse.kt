package settlement.kotlin

sealed class SettlementResponse

data class SuccessResponse<R>(
    val data: R
) : SettlementResponse()

data class PageResponse<R>(
    val list: List<R>,
    val size: Int,
    val offset: Int,
    val totalCount: Int,
    val hasNext: Boolean
) : SettlementResponse()

data class ErrorResponse(
    val message: String
) : SettlementResponse()
