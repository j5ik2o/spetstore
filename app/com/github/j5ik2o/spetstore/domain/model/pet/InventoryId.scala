package com.github.j5ik2o.spetstore.domain.model.pet

import java.util.UUID
import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier

case class InventoryId(value: UUID)
  extends Identifier[UUID]
