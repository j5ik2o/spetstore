package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.application.controller.CategoryController
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.{ CategoryId, Category }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * [[Category]]のJSON表現を表したモデル。
 *
 * @param id ID
 * @param name 名前
 * @param description 説明
 */
case class CategoryJson(
  id: Option[String],
  name: String,
  description: Option[String],
  version: Option[Long]
)

/**
 * [[CategoryJson]]のためのトレイト。
 */
trait CategoryJsonSupport {

  val identifierService: IdentifierService

  protected def convertToEntity(json: CategoryJson): Category =
    Category(
      id = CategoryId(json.id.map(_.toLong).get),
      status = StatusType.Enabled,
      name = json.name,
      description = json.description,
      version = json.version
    )

  protected def convertToEntityWithoutId(json: CategoryJson): Category =
    Category(
      id = CategoryId(identifierService.generate),
      status = StatusType.Enabled,
      name = json.name,
      description = json.description,
      version = json.version
    )

  implicit object JsonConverter extends Reads[CategoryJson] with Writes[Category] {

    override def writes(o: Category): JsValue = JsObject(
      Seq(
        "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
        "name" -> JsString(o.name),
        "description" -> o.description.map(JsString).getOrElse(JsNull),
        "version" -> o.version.fold[JsValue](JsNull)(e => JsString(e.toString))
      )
    )

    override def reads(json: JsValue): JsResult[CategoryJson] =
      ((__ \ 'id).readNullable[String] and
        (__ \ 'name).read[String] and
        (__ \ 'description).readNullable[String] and
        (__ \ 'version).readNullable[String].map(_.map(_.toLong)))(CategoryJson.apply _).reads(json)

  }

}
