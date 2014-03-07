package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import java.util.UUID

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Supplier]]のための識別子。
 *
 * @param value 識別子の値
 */
case class SupplierId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
