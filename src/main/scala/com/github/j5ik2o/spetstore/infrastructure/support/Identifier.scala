package com.github.j5ik2o.spetstore.infrastructure.support

/**
 * [[com.github.j5ik2o.spetstore.infrastructure.support.Entity]]の識別子を表すトレイト。
 *
 * @tparam A 識別子の値型
 */
trait Identifier[+A] {

  /**
   * 識別子の値。
   */
  val value: A

  override def equals(obj: Any) = obj match {
    case that: Identifier[_] =>
      value == that.value
    case _ => false
  }

  override def hashCode = 31 * value.##

}

