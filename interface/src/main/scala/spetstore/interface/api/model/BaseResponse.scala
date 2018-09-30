package spetstore.interface.api.model

trait BaseResponse[A] {
  def isSuccess: Boolean = body.isRight
  def body: Either[ErrorResponseBody, A]
}
