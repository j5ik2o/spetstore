package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Identifier
import java.util.UUID

/**
 * [[com.github.j5ik2o.spetstore.domain.pet.Supplier]]のための識別子。
 *
 * @param value 識別子の値
 */
case class SupplierId(value: UUID) extends Identifier[UUID]
