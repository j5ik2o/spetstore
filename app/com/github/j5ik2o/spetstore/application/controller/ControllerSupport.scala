package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.domain.support.support._
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import org.json4s.DefaultFormats
import play.api.Logger
import play.api.data.validation.ValidationError
import play.api.libs.json.Json._
import play.api.mvc._
import scala.util.{Try, Success}
import scalikejdbc.DB

trait ControllerSupport[ID <: Identifier[Long], E <: Entity[ID], J]
  extends Controller {

  val identifierService: IdentifierService

  val repository: Repository[ID, E] with MultiIOSupport[ID, E]

  implicit val formats = DefaultFormats

  import play.api.libs.json._

  protected def withTransaction[T](f: EntityIOContext => T): T = {
    DB.localTx {
      implicit s =>
        f(EntityIOContextOnJDBC(s))
    }
  }

  protected def convertToEntity(json: J): E

  protected def convertToEntityWithoutId(json: J): E

  protected def createAction(implicit tjs: Writes[E], rds: Reads[J], ctx: EntityIOContext) = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.validate[J].fold(defaultErrorHandler, {
            validatedJson =>
              repository.store(convertToEntityWithoutId(validatedJson)).map {
                case (_, entity) =>
                  Logger.debug(entity.toString)
                  OkForCreatedEntity(entity.id)
              }.recover {
                case ex =>
                  BadRequestForIOError(ex)
              }.get
          })
      }.getOrElse(InternalServerError)
  }

  protected def getAction(id: Long)(apply: Long => ID)
                         (implicit tjs: Writes[E], ctx: EntityIOContext) = Action {
    val identifier = apply(id)
    repository.resolveById(identifier).map {
      entity =>
        Ok(prettyPrint(toJson(entity)))
    }.recoverWith {
      case ex: EntityNotFoundException =>
        Success(NotFoundForEntity(identifier))
    }.getOrElse(InternalServerError)
  }

  protected def listAction(implicit tjs: Writes[E], ctx: EntityIOContext) = Action {
    request =>
      val offset = request.getQueryString("offset").map(_.toInt).getOrElse(0)
      val limit = request.getQueryString("limit").map(_.toInt).getOrElse(100)
      repository.resolveByOffsetWithLimit(offset, limit).map {
        entities =>
          Ok(prettyPrint(JsArray(entities.map(toJson(_)))))
      }.getOrElse(InternalServerError)
  }

  protected def updateAction(id: Long)(apply: Long => ID)
                            (implicit tjs: Writes[E], rds: Reads[J], ctx: EntityIOContext) = Action {
    request =>
      withTransaction {
        implicit ctx =>
          val identifier = apply(id)
          repository.existById(identifier).map {
            exist =>
              if (exist) {
                request.body.asJson.map {
                  json =>
                    json.validate[J].fold(defaultErrorHandler, {
                      validatedJson =>
                        repository.store(convertToEntity(validatedJson)).map {
                          case (_, entity) =>
                            OkForCreatedEntity(entity.id)
                        }.recover {
                          case ex =>
                            BadRequestForIOError(ex)
                        }.get
                    })
                }.getOrElse(InternalServerError)
              } else {
                NotFoundForEntity(identifier)
              }
          }.getOrElse(InternalServerError)
      }
  }

  protected def deleteAction(id: Long)(apply: Long => ID)(implicit tjs: Writes[E], ctx: EntityIOContext) = Action {
    val identifier = apply(id)
    Logger.debug("delete id = "+identifier)
    repository.deleteById(identifier).map {
      case (_, entity) =>
        Logger.debug("deleted id = "+identifier)
        Ok(prettyPrint(toJson(entity)))
    }.recoverWith {
      case ex: EntityNotFoundException =>
        Success(NotFoundForEntity(identifier))
    }.getOrElse(InternalServerError)
  }

  protected val defaultErrorHandler = {
    error: Seq[(JsPath, Seq[ValidationError])] =>
      BadRequestForValidate(JsError.toFlatJson(error))
  }

  protected def createErrorResponse(message: String) = JsObject(
    Seq(
      "message" -> JsString(message)
    )
  )

  protected val OkForCreatedEntity = {
    id: Identifier[Long] =>
      Ok(
        JsObject(
          Seq(
            "id" -> JsString(id.value.toString)
          )
        )
      )
  }

  protected val BadRequestForIOError = { ex: Throwable => BadRequest(createErrorResponse("IO Error : "+ex.toString)) }

  protected val BadRequestForValidate = {
    param: JsObject => BadRequest(createErrorResponse(s"Validate Error: $param"))
  }

  protected val NotFoundForEntity = {
    id: Identifier[Long] => NotFound(s"identifier = $id")
  }

}
