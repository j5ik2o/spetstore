package com.j5ik2o.spetstore.infrastructure.support

/**
 * Created by junichi on 2014/01/05.
 */
case class Person(id: PersonId, firstName: String, lastName: String)
  extends Entity[PersonId]
