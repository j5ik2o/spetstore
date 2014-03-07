package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import java.util.UUID

/**
 * 商品区分の識別子。
 *
 * @param value 識別子の値。
 */
case class ItemTypeId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
