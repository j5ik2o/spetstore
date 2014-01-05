package com.github.j5ik2o.spetstore.infrastructure.support

/**
 * テスト用のエンティティ。
 */
case class Person(id: PersonId, firstName: String, lastName: String)
  extends Entity[PersonId]
