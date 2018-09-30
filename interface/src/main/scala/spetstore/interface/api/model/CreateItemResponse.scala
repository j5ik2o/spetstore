package spetstore.interface.api.model

case class CreateItemResponse(override val body: Either[ErrorResponseBody, CreateItemResponseBody])
    extends BaseResponse[CreateItemResponseBody]
