package com.github.j5ik2o.spetstore.domain.model.customer

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import com.github.j5ik2o.spetstore.domain.lifecycle.IdentifierService

/**
 * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CustomerId(value: Long = IdentifierService.generate(classOf[Customer]))
  extends Identifier[Long]
