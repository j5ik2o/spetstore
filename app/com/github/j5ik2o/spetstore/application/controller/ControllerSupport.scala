package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import com.github.tototoshi.play2.json4s.jackson.Json4s
import org.json4s.DefaultFormats
import play.api.mvc.Controller

trait ControllerSupport extends Controller with Json4s {

  implicit val formats = DefaultFormats

  import play.api.libs.json._

  protected def createErrorResponse(message: String) = JsObject(
    Seq(
      "message" -> JsString(message)
    )
  )

  protected val OkForCreatedEntity = {
    id: Identifier[Long] => Ok(
      JsObject(
        Seq(
          "id" -> JsNumber(id.value)
        )
      )
    )
  }

  protected val BadRequestForIOError = BadRequest(createErrorResponse("IO Error"))

  protected val BadRequestForValidate = {
    param: JsObject => BadRequest(createErrorResponse(s"Validate Error: $param"))
  }

  protected val NotFoundForEntity = {
    id: Identifier[Long] => NotFound(s"identifier = $id")
  }

}
