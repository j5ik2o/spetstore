package com.github.j5ik2o.spetstore.infrastructure.support

trait Identifier[A]{

  val value: A

  override def equals(obj: Any) = obj match {
    case that: Identifier[_] =>
      value == that.value
    case _ => false
  }

  override def hashCode = 31 * value.##

  override def toString = s"Identifier($value)"


}

