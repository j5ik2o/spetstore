package com.github.j5ik2o.spetstore.infrastructure.support

import java.util.UUID

/**
 * テスト用の識別子。
 */
case class PersonId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
