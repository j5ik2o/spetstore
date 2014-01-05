package com.github.j5ik2o.spetstore.domain.customer

import com.github.j5ik2o.spetstore.infrastructure.support.Identifier
import java.util.UUID

/**
 * [[com.github.j5ik2o.spetstore.domain.customer.Customer]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CustomerId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
