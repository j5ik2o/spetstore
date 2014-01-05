package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

trait InventoryRepository extends Repository[InventoryId, Inventory] {

  type This = InventoryRepository

}

object InventoryRepository {

  def ofMemory(entities: Map[InventoryId, Inventory] = Map.empty): InventoryRepository =
    new InventoryRepositoryOnMemory(entities)

}

