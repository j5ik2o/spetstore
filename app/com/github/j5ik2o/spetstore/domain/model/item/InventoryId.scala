package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import com.github.j5ik2o.spetstore.domain.lifecycle.IdentifierService

case class InventoryId(value: Long = IdentifierService.generate(classOf[Inventory]))
  extends Identifier[Long]
