package com.github.j5ik2o.spetstore.infrastructure.support

trait Entity[ID <: Identifier[_]] {
  val id: ID

  override def equals(obj: Any): Boolean = this match {
    case that: Entity[_] => id == that.id
    case _ => false
  }

  override def hashCode: Int = 31 * id.##

}
