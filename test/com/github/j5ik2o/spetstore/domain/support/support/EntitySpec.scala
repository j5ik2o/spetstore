package com.github.j5ik2o.spetstore.domain.support.support

import org.scalatest.{FunSpec, Matchers}

class EntitySpec extends FunSpec with Matchers {

  case class EntityAId(value: Long)
    extends Identifier[Long]

  case class EntityA(id: EntityAId, name: String, version: Option[Long])
    extends Entity[EntityAId] {
    override def canEqual(other: Any) = other.isInstanceOf[EntityA]
    override def withVersion(version: Long): EntityA =
      copy(version = Some(version))
  }

  case class EntityBId(value: Long)
    extends Identifier[Long]

  case class EntityB(id: EntityBId, name: String, version: Option[Long])
    extends Entity[EntityBId] {
    override def canEqual(other: Any) = other.isInstanceOf[EntityB]
    override def withVersion(version: Long): EntityB =
      copy(version = Some(version))
  }

  describe("EntityA") {
    it("should not be EntityB") {
      val a = EntityA(EntityAId(1L), "a1", Some(1L))
      val b = EntityB(EntityBId(2L), "b1", Some(1L))
      a should not be b
    }
    it("should be EntityA with version 1") {
      val a = EntityA(EntityAId(1L), "a1", None)
      a should be (a.withVersion(1))
    }
  }

}
