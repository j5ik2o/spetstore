package com.github.j5ik2o.spetstore.domain.infrastructure.support

import scala.util.Try
import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.infrastructure.db.CRUDMapper
import play.Logger

/**
 * JDBC用[[com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContext]]。
 *
 * @param session DBセッション
 */
case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

trait DaoSupport[ID, M, T] {

  protected val mapper: CRUDMapper[T]

  protected def convertToPrimaryKey(id: ID): Long

  protected def convertToRecord(model: M): T

  protected def convertToEntity(record: T): M

  def insertOrUpdate(id: ID, model: M)(implicit s: DBSession) = Try {
    val count = mapper.updateById(convertToPrimaryKey(id))
      .withAttributes(mapper.toNamedValues(convertToRecord(model)).filterNot {
      case (k, _) => k.name == mapper.primaryKeyFieldName
    }: _*)
    if (count == 0) mapper.createWithAttributes(mapper.toNamedValues(convertToRecord(model)): _*)
    else if (count > 1) throw new IllegalStateException(s"$count tables are found for identifier: $id")
    model
  }

  def findById(id: ID)(implicit s: DBSession): Try[M] = Try {
    mapper.findById(convertToPrimaryKey(id)).map(convertToEntity).getOrElse(throw new EntityNotFoundException(s"$id"))
  }

  def findAllWithLimitOffset(limit: Int, offset: Int)(implicit s: DBSession): Try[Seq[M]] = Try {
    mapper.findAllWithLimitOffset(limit, offset).map(convertToEntity)
  }

  def deleteById(id: ID)(implicit s: DBSession): Try[M] = findById(id).map {
    entity =>
      if (mapper.deleteById(convertToPrimaryKey(id)) == 0) {
        throw new RepositoryIOException("")
      } else {
        entity
      }
  }

}


trait SimpleRepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]] extends RepositoryOnJDBC[ID, E] {
  self =>

  private object MainService extends DaoSupport[Identifier[Long], E, T] {
    override protected val mapper = self.mapper

    override protected def convertToRecord(model: E) = self.convertToRecord(model)

    override protected def convertToEntity(record: T) = self.convertToEntity(record)

    override protected def convertToPrimaryKey(id: Identifier[Long]): Long = id.value
  }

  protected def convertToEntity(record: T): E

  protected def convertToRecord(entity: E): T

  override def resolveEntity(identifier: ID)(implicit ctx: Ctx): Try[E] = withDBSession(ctx) {
    implicit s =>
      MainService.findById(identifier)
  }

  override def storeEntity(entity: E)(implicit ctx: Ctx): Try[(This, E)] = withDBSession(ctx) {
    implicit s =>
      MainService.insertOrUpdate(entity.id, entity).map {
        entity =>
          (this.asInstanceOf[This], entity)
      }
  }

  override def deleteByIdentifier(identifier: ID)(implicit ctx: Ctx): Try[(This, E)] = withDBSession(ctx) {
    implicit s =>
      MainService.deleteById(identifier).map {
        entity =>
          (this.asInstanceOf[This], entity)
      }
  }


  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[E]] = withDBSession(ctx) {
    implicit s =>
      Logger.debug(s"offset = $offset, limit = $limit")
      val r = MainService.findAllWithLimitOffset(limit, offset)
      Logger.debug(r.toString)
      r
  }

}

/**
 * JDBC用リポジトリのための骨格実装。
 */
abstract class RepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]]
  extends Repository[ID, E] {

  type T

  protected val mapper: CRUDMapper[T]

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): A = {
    ctx match {
      case EntityIOContextOnJDBC(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

  def existByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] = withDBSession(ctx) {
    implicit s => Try {
      val count = mapper.countBy(sqls.eq(mapper.defaultAlias.field(mapper.primaryKeyFieldName), identifier.value))
      if (count == 0) false
      else if (count == 1) true
      else throw new IllegalStateException(s"$count entities are found for identifier: $identifier")
    }
  }

  override def existByIdentifiers(identifiers: ID*)(implicit ctx: Ctx): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      Try(mapper.countBy(sqls.in(mapper.defaultAlias.field(mapper.primaryKeyFieldName), identifiers.map(_.value))) > 0)
  }

}
