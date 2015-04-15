package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.Identifier
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

case class InventoryId(value: Long)
  extends Identifier[Long]
