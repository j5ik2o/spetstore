package com.github.j5ik2o.spetstore.infrastructure.support

/**
 * エンティティが見つからなかった場合の例外。
 *
 * @param identifier 見つからなかったエンティティの識別子
 * @tparam ID 識別子の型
 */
case class EntityNotFoundException[ID <: Identifier[_]](identifier: ID)
  extends Exception(s"Entity is not found(identifier = $identifier)")

