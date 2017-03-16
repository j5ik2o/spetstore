package com.github.j5ik2o.spetstore.domain.support.support

/**
 * [[com.github.j5ik2o.spetstore.domain.support.support.Entity]]の識別子を表すトレイト。
 *
 * @tparam A 識別子の値型
 */
trait Identifier[+A] {

  /**
   * 識別子の値。
   */
  def value: A

  val isDefined: Boolean = true

  val isEmpty: Boolean = !isDefined

  override def equals(obj: Any) = obj match {
    case that: Identifier[_] =>
      value == that.value
    case _ => false
  }

  override def hashCode: Int = 31 * value.##

}

object EmptyIdentifier extends EmptyIdentifier

trait EmptyIdentifier extends Identifier[Nothing] {

  def value: Nothing = throw new NoSuchElementException

  override val isDefined = false

  override def equals(obj: Any) = obj match {
    case that: EmptyIdentifier => this eq that
    case _ => false
  }

  override def hashCode = 0

}

