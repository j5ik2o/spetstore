package com.j5ik2o.spetstore.infrastructure.support

import java.util.UUID

/**
 * Created by junichi on 2014/01/05.
 */
case class PersonId(value: UUID = UUID.randomUUID())
  extends Identifier[UUID]
