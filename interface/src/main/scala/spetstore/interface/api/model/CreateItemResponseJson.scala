package spetstore.interface.api.model

case class CreateItemResponseJson(override val body: Either[ErrorResponseBody, CreateItemResponseBody])
    extends BaseResponse[CreateItemResponseBody]
