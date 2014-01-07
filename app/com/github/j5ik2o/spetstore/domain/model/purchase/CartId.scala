package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import java.util.UUID

/**
 * [[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CartId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]

