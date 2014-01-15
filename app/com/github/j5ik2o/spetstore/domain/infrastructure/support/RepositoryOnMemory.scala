package com.github.j5ik2o.spetstore.domain.infrastructure.support

import scala.util.{Success, Try}

/**
 * メモリ用[[com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContext]]。
 */
case object EntityIOContextOnMemory extends EntityIOContext

/**
 * メモリ用リポジトリのための骨格実装。
 *
 * @param entities エンティティのマップ
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
abstract class RepositoryOnMemory[ID <: Identifier[_], E <: Entity[ID]]
(entities: Map[ID, E])
  extends Repository[ID, E] {

  /**
   * 新しいインスタンスを生成する。
   *
   * @param entities エンティティのマップ
   * @return 新しいインスタンス
   */
  protected def createInstance(entities: Map[ID, E]): This

  def existByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] =
    Success(entities.contains(identifier))

  def resolveEntity(identifier: ID)(implicit ctx: EntityIOContext): Try[E] = Try {
    entities.get(identifier).getOrElse(throw EntityNotFoundException(identifier))
  }

  def storeEntity(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)] = Success {
    (createInstance(entities + (entity.id -> entity)), entity)
  }

  def deleteByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)] = Try {
    entities.get(identifier).map {
      entity =>
        (createInstance(entities - identifier), entity)
    }.getOrElse(throw EntityNotFoundException(identifier))
  }

}
