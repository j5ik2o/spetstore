package spetstore.interface.api.rejection

import akka.http.scaladsl.server.RejectionWithOptionalCause

case class MalformedPathRejection(pathName: String, errorMsg: String, cause: Option[Throwable] = None)
    extends RejectionWithOptionalCause
