package com.j5ik2o.spetstore.infrastructure.support

import scala.util.Try

/**
 * DDDのリポジトリ責務を表すトレイト。
 *
 * @tparam ID
 * @tparam E
 */
trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  type This <: Repository[ID, E]

  def contains(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean]

  def resolve(identifier: ID)(implicit ctx: EntityIOContext): Try[E]

  def store(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)]

  def delete(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)]

}
