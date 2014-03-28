package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import java.util.UUID
import com.github.j5ik2o.spetstore.domain.lifecycle.IdentifierService
import com.github.j5ik2o.spetstore.domain.model.customer.Customer

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Category]]のための識別子。
 *
 * @param value 識別子の値
 */
case class CategoryId(value: Long = IdentifierService.generate(classOf[Category]))
  extends Identifier[Long]
