package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.domain.support.support._
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import io.circe.syntax._
import io.circe.{ Decoder, Encoder, Json }
import play.api.Logger
import play.api.libs.circe.Circe
import play.api.mvc._
import scalikejdbc.DB

import scala.util.{ Success, Try }

trait ControllerSupport[ID <: Identifier[Long], E <: Entity[ID], J]
    extends Controller with Circe {

  val identifierService: IdentifierService

  val repository: Repository[ID, E] with MultiIOSupport[ID, E]

  protected def withTransaction[T](f: EntityIOContext => Try[T]): Try[T] = {
    Try {
      DB.localTx[T] {
        implicit s =>
          f(EntityIOContextOnJDBC(s)).get
      }
    }
  }

  protected def convertToEntity(json: J): E

  protected def convertToEntityWithoutId(json: J): E

  protected def convertToJson(entity: E): J

  protected def createAction(implicit decoder: Decoder[J]): Action[J] = Action(circe.json[J]) {
    request =>
      withTransaction {
        implicit ctx =>
          repository.store(convertToEntityWithoutId(request.body)).map {
            case (_, entity) =>
              Logger.debug(entity.toString)
              OkForCreatedEntity(entity.id)
          }.recover {
            case ex =>
              BadRequestForIOError(ex)
          }
      }.get
  }

  protected def getAction(id: Long)(apply: Long => ID)(implicit ctx: EntityIOContext, encoder: Encoder[J]) = Action {
    val identifier = apply(id)
    repository.resolveById(identifier).map {
      entity =>
        Ok(convertToJson(entity).asJson)
    }.recoverWith {
      case _: EntityNotFoundException =>
        Success(NotFoundForEntity(identifier))
    }.getOrElse(InternalServerError)
  }

  protected def listAction(implicit encoder: Encoder[J], ctx: EntityIOContext) = Action {
    request =>
      val offset = request.getQueryString("offset").map(_.toInt).getOrElse(0)
      val limit = request.getQueryString("limit").map(_.toInt).getOrElse(100)
      repository.resolveByOffsetWithLimit(offset, limit).map {
        entities =>
          Ok(entities.map(convertToJson).asJson)
      }.getOrElse(InternalServerError)
  }

  protected def updateAction(id: Long)(apply: Long => ID)(implicit decoder: Decoder[J], ctx: EntityIOContext): Action[J] = Action(circe.json[J]) {
    request =>
      val identifier = apply(id)
      repository.existById(identifier).map {
        exist =>
          if (exist) {
            withTransaction {
              implicit ctx =>
                repository.store(convertToEntity(request.body)).map {
                  case (_, entity) =>
                    OkForCreatedEntity(entity.id)
                }.recover {
                  case ex =>
                    BadRequestForIOError(ex)
                }
            }.getOrElse(InternalServerError)
          } else {
            NotFoundForEntity(identifier)
          }
      }.getOrElse(InternalServerError)
  }

  protected def deleteAction(id: Long)(apply: Long => ID)(implicit encoder: Encoder[J], ctx: EntityIOContext) = Action {
    val identifier = apply(id)
    Logger.debug("delete id = " + identifier)
    repository.deleteById(identifier).map {
      case (_, entity) =>
        Logger.debug("deleted id = " + identifier)
        Ok(convertToJson(entity).asJson)
    }.recoverWith {
      case _: EntityNotFoundException =>
        Success(NotFoundForEntity(identifier))
    }.getOrElse(InternalServerError)
  }

  protected def createErrorResponse(message: String): Json =
    Seq(
      "message" -> message
    ).asJson

  protected val OkForCreatedEntity: (Identifier[Long]) => Result = {
    id: Identifier[Long] =>
      Ok(
        Map("id" -> id.value.toString).asJson
      )
  }

  protected val BadRequestForIOError: (Throwable) => Result = { ex: Throwable => BadRequest(createErrorResponse("IO Error : " + ex.toString)) }

  protected val NotFoundForEntity: (Identifier[Long]) => Result = {
    id: Identifier[Long] => NotFound(s"identifier = $id")
  }

}
