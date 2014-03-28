package com.github.j5ik2o.spetstore.domain.infrastructure.support

import scala.util.Try
import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper

/**
 * JDBC用[[com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContext]]。
 *
 * @param session DBセッション
 */
case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

trait SimpleRepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]] extends RepositoryOnJDBC[ID, E] {
  type TS = T

  protected def convertToEntity(record: TS): E

  protected def convertToRecord(entity: E): TS

  override def resolveEntity(identifier: ID)(implicit ctx: Ctx): Try[E] = Try {
    mapper.findById(identifier.value).map(convertToEntity).getOrElse(throw EntityNotFoundException(identifier))
  }

  override def storeEntity(entity: E)(implicit ctx: Ctx): Try[(This, E)] = Try {
    val count = mapper.updateById(entity.id.value)
      .withAttributes(mapper.toNamedValues(convertToRecord(entity)).filterNot {
      case (k, _) => k.name == mapper.primaryKeyFieldName
    }: _*)
    if (count == 0) mapper.createWithAttributes(mapper.toNamedValues(convertToRecord(entity)): _*)
    else if (count > 1) throw new IllegalStateException(s"$count entities are found for identifier: $entity.id")
    (this.asInstanceOf[This], entity)
  }

  override def deleteByIdentifier(identifier: ID)(implicit ctx: Ctx): Try[(This, E)] = identifier.synchronized {
    resolveEntity(identifier).map {
      entity =>
        if (mapper.deleteById(identifier.value) == 0) {
          throw new RepositoryIOException("")
        } else {
          (this.asInstanceOf[This], entity)
        }
    }
  }

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[E]] = Try {
    mapper.findAllWithLimitOffset(offset, limit).map(convertToEntity)
  }

}

/**
 * JDBC用リポジトリのための骨格実装。
 */
abstract class RepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]]
  extends Repository[ID, E] {

  type T

  protected val mapper: CRUDMapper[T]

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): Try[A] = Try {
    ctx match {
      case EntityIOContextOnJDBC(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

  def existByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      val count = mapper.countBy(sqls.eq(mapper.defaultAlias.field(mapper.primaryKeyFieldName), identifier.value))
      if (count == 0) false
      else if (count == 1) true
      else throw new IllegalStateException(s"$count entities are found for identifier: $identifier")
  }

  override def existByIdentifiers(identifiers: ID*)(implicit ctx: Ctx): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      mapper.countBy(sqls.in(mapper.defaultAlias.field(mapper.primaryKeyFieldName), identifiers.map(_.value))) > 0
  }

}
