package spetstore.interface.api.model

case class CreateCartResponseJson(override val body: Either[ErrorResponseBody, CreateCartResponseBody])
    extends BaseResponse[CreateCartResponseBody]
