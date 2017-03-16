package com.github.j5ik2o.spetstore.domain.support.support

/**
 * DDDのエンティティ責務を表すトレイト。
 *
 * @tparam ID [[Identifier]]
 */
trait Entity[ID <: Identifier[_]] {

  /**
   * 識別子。
   */
  val id: ID

  def canEqual(other: Any): Boolean

  override def equals(obj: Any): Boolean = obj match {
    case that: Entity[_] =>
      that.canEqual(this) && id == that.id
    case _ => false
  }

  override def hashCode: Int = 31 * id.##

  val version: Option[Long]

  def withVersion(version: Long): Entity[ID]
}
