package com.github.j5ik2o.spetstore.domain.support.support

/**
 * エンティティが見つからなかった場合の例外。
 */
case class EntityNotFoundException(message: String) extends Exception(message)

object EntityNotFoundException {

  def apply(identifier: Identifier[Any]): EntityNotFoundException =
    EntityNotFoundException(s"Entity is not found(identifier = $identifier)")

}
