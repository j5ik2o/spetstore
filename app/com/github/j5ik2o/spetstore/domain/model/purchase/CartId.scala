package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import com.github.j5ik2o.spetstore.domain.lifecycle.IdentifierService

/**
 * [[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CartId(value: Long = IdentifierService.generate(classOf[Cart]))
  extends Identifier[Long]

