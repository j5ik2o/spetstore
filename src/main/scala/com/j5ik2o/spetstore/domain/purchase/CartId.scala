package com.j5ik2o.spetstore.domain.purchase

import com.j5ik2o.spetstore.infrastructure.support.Identifier
import java.util.UUID

/**
 * [[com.j5ik2o.spetstore.domain.purchase.Cart]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CartId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]

