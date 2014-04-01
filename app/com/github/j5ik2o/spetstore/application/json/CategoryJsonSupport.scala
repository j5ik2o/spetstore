package com.github.j5ik2o.spetstore.application.json

import com.github.j5ik2o.spetstore.application.controller.CategoryController
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.{CategoryId, Category}
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CategoryJson(id: Option[String],
                        name: String,
                        description: Option[String])

trait CategoryJsonSupport {
  this: CategoryController =>

  protected def convertToEntity(json: CategoryJson): Category =
    Category(
      id = CategoryId(json.id.map(_.toLong).get),
      status = StatusType.Enabled,
      name = json.name,
      description = json.description
    )

  protected def convertToEntityWithoutId(json: CategoryJson): Category =
    Category(
      id = CategoryId(identifierService.generate),
      status = StatusType.Enabled,
      name = json.name,
      description = json.description
    )


  implicit object JsonConverter extends Reads[CategoryJson] with Writes[Category] {

    override def writes(o: Category): JsValue = JsObject(
      Seq(
        "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
        "name" -> JsString(o.name),
        "description" -> o.description.map(JsString).getOrElse(JsNull)
      )
    )

    override def reads(json: JsValue): JsResult[CategoryJson] =
      ((__ \ 'id).readNullable[String] and
        (__ \ 'name).read[String] and
        (__ \ 'description).readNullable[String])(CategoryJson.apply _).reads(json)

  }

}
