package com.github.j5ik2o.spetstore.domain.model.customer

import com.github.j5ik2o.spetstore.domain.support.support.Identifier

/**
 * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CustomerId(value: Long)
  extends Identifier[Long]
