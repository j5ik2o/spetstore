package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.Identifier
import java.util.UUID

/**
 * 商品区分の識別子。
 *
 * @param value 識別子の値。
 */
case class ItemTypeId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
