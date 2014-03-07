package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Entity

/**
 * 在庫を表すエンティティ。
 *
 * @param id [[com.github.j5ik2o.spetstore.domain.model.item.InventoryId]]
 * @param petId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
 * @param quantity 在庫数量
 */
case class Inventory(id: InventoryId, petId: ItemId, quantity: Int)
  extends Entity[InventoryId]

