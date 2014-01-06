package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Entity

/**
 * 在庫を表すエンティティ。
 *
 * @param id [[com.github.j5ik2o.spetstore.domain.pet.InventoryId]]
 * @param petId [[com.github.j5ik2o.spetstore.domain.pet.PetId]]
 * @param quantity 在庫数量
 */
case class Inventory(id: InventoryId, petId: PetId, quantity: Int)
  extends Entity[InventoryId]

