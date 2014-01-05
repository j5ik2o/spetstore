package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Entity

case class Inventory(id: InventoryId, petId: PetId, quantity: Int)
  extends Entity[InventoryId]

