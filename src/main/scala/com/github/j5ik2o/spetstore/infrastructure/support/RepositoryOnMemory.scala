package com.github.j5ik2o.spetstore.infrastructure.support

import scala.util.{Success, Try}

case object EntityIOContextOnMemory extends EntityIOContext

abstract class RepositoryOnMemory[ID <: Identifier[_], E <: Entity[ID]]
(entities: Map[ID, E])
  extends Repository[ID, E] {

  protected def createInstance(entities: Map[ID, E]): This

  def contains(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] =
    Success(entities.contains(identifier))

  def resolve(identifier: ID)(implicit ctx: EntityIOContext): Try[E] = Try {
    entities.get(identifier).getOrElse(throw EntityNotFoundException(identifier))
  }

  def store(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)] = Success {
    (createInstance(entities + (entity.id -> entity)), entity)
  }

  def delete(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)] = Try {
    entities.get(identifier).map {
      entity =>
        (createInstance(entities - identifier), entity)
    }.getOrElse(throw EntityNotFoundException(identifier))
  }

}
