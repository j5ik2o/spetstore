package com.github.j5ik2o.spetstore.domain.model.item

import java.util.UUID
import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Item]]のための識別子
 *
 * @param value 識別子の値
 */
case class ItemId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
