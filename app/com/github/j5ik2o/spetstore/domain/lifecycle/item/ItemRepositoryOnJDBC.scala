package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.{ Item, ItemId, ItemTypeId, SupplierId }
import com.github.j5ik2o.spetstore.domain.support.support.SimpleRepositoryOnJDBC
import com.github.j5ik2o.spetstore.infrastructure.db.ItemRecord

private[item] class ItemRepositoryOnJDBC
    extends SimpleRepositoryOnJDBC[ItemId, Item] with ItemRepository {

  override type T = ItemRecord

  override protected lazy val mapper = ItemRecord

  override protected def convertToEntity(record: ItemRecord): Item = Item(
    id = ItemId(record.id),
    status = StatusType.Enabled,
    itemTypeId = ItemTypeId(record.itemTypeId),
    name = record.name,
    description = record.description,
    price = record.price,
    supplierId = SupplierId(record.supplierId),
    version = Some(record.version)
  )

  override protected def convertToRecord(entity: Item): ItemRecord = ItemRecord(
    id = entity.id.value,
    status = entity.status.id,
    itemTypeId = entity.itemTypeId.value,
    name = entity.name,
    description = entity.description,
    price = entity.price,
    supplierId = entity.supplierId.value,
    version = entity.version.getOrElse(1)
  )

}
