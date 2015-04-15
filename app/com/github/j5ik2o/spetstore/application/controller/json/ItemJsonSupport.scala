package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.application.controller.ItemController
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.item._
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ItemJson(id: Option[String],
                    itemTypeId: String,
                    name: String,
                    description: Option[String],
                    price: String,
                    supplierId: String,
                    version: Option[String])

trait ItemJsonSupport {
  this: ItemController =>

  protected def convertToEntity(itemJson: ItemJson): Item =
    Item(
      id = ItemId(itemJson.id.map(_.toLong).get),
      status = StatusType.Enabled,
      itemTypeId = ItemTypeId(itemJson.itemTypeId.toLong),
      name = itemJson.name,
      description = itemJson.description,
      price = BigDecimal(itemJson.price),
      supplierId = SupplierId(itemJson.supplierId.toLong),
      version = itemJson.version.map(_.toLong)
    )

  protected def convertToEntityWithoutId(itemJson: ItemJson): Item =
    Item(
      id = ItemId(identifierService.generate),
      status = StatusType.Enabled,
      itemTypeId = ItemTypeId(itemJson.itemTypeId.toLong),
      name = itemJson.name,
      description = itemJson.description,
      price = BigDecimal(itemJson.price),
      supplierId = SupplierId(itemJson.supplierId.toLong),
      version = itemJson.version.map(_.toLong)
    )

  implicit object JsonConverter extends Reads[ItemJson] with Writes[Item] {

    def reads(json: JsValue): JsResult[ItemJson] = {
      ((__ \ 'id).readNullable[String] and
        (__ \ 'itemTypeId).read[String] and
        (__ \ 'name).read[String] and
        (__ \ 'description).readNullable[String] and
        (__ \ 'price).read[String] and
        (__ \ 'supplierId).read[String] and
        (__ \ 'version).readNullable[String])(ItemJson.apply _).reads(json)
    }

    override def writes(o: Item): JsValue = {
      JsObject(
        Seq(
          "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
          "itemTypeId" -> JsString(o.itemTypeId.toString),
          "name" -> JsString(o.name),
          "description" -> o.description.fold[JsValue](JsNull)(JsString),
          "price" -> JsString(o.price.toString()),
          "supplierId" -> JsString(o.supplierId.toString),
          "version" -> o.version.fold[JsValue](JsNull)(e => JsString(e.toString))
        )
      )
    }

  }

}
