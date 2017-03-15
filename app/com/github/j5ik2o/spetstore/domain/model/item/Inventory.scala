package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.Entity
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType

/**
 * 在庫を表すエンティティ。
 *
 * @param id [[com.github.j5ik2o.spetstore.domain.model.item.InventoryId]]
 * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
 * @param quantity 在庫数量
 */
case class Inventory(
  id: InventoryId,
  status: StatusType.Value,
  itemId: ItemId,
  quantity: Int,
  version: Option[Long]
)
    extends Entity[InventoryId] {

  override def withVersion(version: Long): Entity[InventoryId] =
    copy(version = Some(version))

}

