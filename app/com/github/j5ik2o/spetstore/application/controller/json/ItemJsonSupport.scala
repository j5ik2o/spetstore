package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.item._
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

case class ItemJson(
  id: Option[String],
  itemTypeId: Long,
  name: String,
  description: Option[String],
  price: String,
  supplierId: Long,
  version: Option[Long]
)

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

  protected def convertToJson(entity: Item): ItemJson = ItemJson(
    id = Some(entity.id.value.toString),
    itemTypeId = entity.itemTypeId.value,
    name = entity.name,
    description = entity.description,
    price = entity.price.toString(),
    supplierId = entity.supplierId.value,
    version = entity.version
  )

}
