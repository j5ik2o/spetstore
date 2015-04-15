package com.github.j5ik2o.spetstore.domain.support.support

/**
 * テスト用のエンティティ。
 */
case class Person(id: PersonId, firstName: String, lastName: String, version: Option[Long] = None)
  extends Entity[PersonId]{
  override def withVersion(version: Long): Entity[PersonId] = copy(version = Some(version))
}
