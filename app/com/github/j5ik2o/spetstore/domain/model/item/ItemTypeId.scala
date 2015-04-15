package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.Identifier
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

/**
 * 商品区分の識別子。
 *
 * @param value 識別子の値。
 */
case class ItemTypeId(value: Long)
  extends Identifier[Long]
