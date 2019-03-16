package spetstore.interface.api.model

case class ResolveCartResponseJson(override val body: Either[ErrorResponseBody, ResolveCartResponseBody])
    extends BaseResponse[ResolveCartResponseBody]
