package spetstore.interface.api.model

case class ResolveItemResponseJson(override val body: Either[ErrorResponseBody, ResolveItemResponseBody])
    extends BaseResponse[ResolveItemResponseBody]
