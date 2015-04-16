package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.application.controller.ItemController
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.item._
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ItemJson(id: Option[String],
                    itemTypeId: Long,
                    name: String,
                    description: Option[String],
                    price: String,
                    supplierId: Long,
                    version: Option[Long])

trait ItemJsonSupport {
  val identifierService: IdentifierService

  protected def convertToEntity(itemJson: ItemJson): Item =
    Item(
      id = ItemId(itemJson.id.map(_.toLong).get),
      status = StatusType.Enabled,
      itemTypeId = ItemTypeId(itemJson.itemTypeId),
      name = itemJson.name,
      description = itemJson.description,
      price = BigDecimal(itemJson.price),
      supplierId = SupplierId(itemJson.supplierId),
      version = itemJson.version
    )

  protected def convertToEntityWithoutId(itemJson: ItemJson): Item =
    Item(
      id = ItemId(identifierService.generate),
      status = StatusType.Enabled,
      itemTypeId = ItemTypeId(itemJson.itemTypeId),
      name = itemJson.name,
      description = itemJson.description,
      price = BigDecimal(itemJson.price),
      supplierId = SupplierId(itemJson.supplierId),
      version = itemJson.version
    )

  implicit object JsonConverter extends Reads[ItemJson] with Writes[Item] {

    def reads(json: JsValue): JsResult[ItemJson] = {
      ((__ \ 'id).readNullable[String] and
        (__ \ 'itemTypeId).read[String].map(_.toLong) and
        (__ \ 'name).read[String] and
        (__ \ 'description).readNullable[String] and
        (__ \ 'price).read[String] and
        (__ \ 'supplierId).read[String].map(_.toLong) and
        (__ \ 'version).readNullable[String].map(_.map(_.toLong)))(ItemJson.apply _).reads(json)
    }

    override def writes(o: Item): JsValue = {
      JsObject(
        Seq(
          "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
          "itemTypeId" -> JsString(o.itemTypeId.value.toString),
          "name" -> JsString(o.name),
          "description" -> o.description.fold[JsValue](JsNull)(JsString),
          "price" -> JsString(o.price.toString()),
          "supplierId" -> JsString(o.supplierId.value.toString),
          "version" -> o.version.fold[JsValue](JsNull)(e => JsString(e.toString))
        )
      )
    }

  }

}
