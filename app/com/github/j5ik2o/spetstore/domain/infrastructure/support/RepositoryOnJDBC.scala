package com.github.j5ik2o.spetstore.domain.infrastructure.support

import scala.util.Try
import scalikejdbc._, SQLInterpolation._
import skinny.orm.SkinnyCRUDMapper

/**
 * JDBC用[[com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContext]]。
 *
 * @param session DBセッション
 */
case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

/**
 * JDBC用リポジトリのための骨格実装。
 */
abstract class RepositoryOnJDBC[ID <: Identifier[_], E <: Entity[ID]]
  extends Repository[ID, E] {

  abstract class AbstractDao[A <: E] extends SkinnyCRUDMapper[A] {
    override def primaryKeyFieldName = "id"

    override def defaultAlias = createAlias(tableName)

    override def useAutoIncrementPrimaryKey = false

    def toNamedValues(entity: E): Seq[(Symbol, Any)]
  }

  protected def createDao: AbstractDao[E]

  val defaultDao = createDao

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): Try[A] = Try {
    ctx match {
      case EntityIOContextOnJDBC(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

  def existByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      val count = defaultDao.countBy(sqls.eq(defaultDao.defaultAlias.field(defaultDao.primaryKeyFieldName), identifier.value.toString))
      if (count == 0) false
      else if (count == 1) true
      else throw new IllegalStateException(s"$count entities are found for identifier: $identifier")
  }

  override def existByIdentifiers(identifiers: ID*)(implicit ctx: Ctx): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      defaultDao.countBy(sqls.in(defaultDao.defaultAlias.field(defaultDao.primaryKeyFieldName), identifiers.map(_.value))) > 0
  }

  def resolveEntity(identifier: ID)(implicit ctx: EntityIOContext): Try[E] = withDBSession(ctx) {
    implicit s =>
      defaultDao.findBy(sqls.eq(defaultDao.defaultAlias.field(defaultDao.primaryKeyFieldName), identifier.value.toString)).
        getOrElse(throw EntityNotFoundException(identifier))
  }

  override def resolveEntities(identifiers: ID*)(implicit ctx: Ctx): Try[Seq[E]] = withDBSession(ctx) {
    implicit s =>
      defaultDao.findAllBy(sqls.in(defaultDao.defaultAlias.field(defaultDao.primaryKeyFieldName), identifiers.map(_.value.toString)))
  }

  def resolveEntities(offset: Int, limit: Int = 100)(implicit ctx: EntityIOContext): Try[Seq[E]] = withDBSession(ctx) {
    implicit s =>
      defaultDao.findAllWithLimitOffset(limit, offset)
  }

  def storeEntity(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)] = withDBSession(ctx) {
    implicit s =>
      if (entity.id.isDefined) {
        val count = defaultDao.updateBy(sqls.eq(defaultDao.column.field(defaultDao.primaryKeyFieldName), entity.id.value.toString))
          .withAttributes(defaultDao.toNamedValues(entity).filterNot {
          case (k, _) => k.name == defaultDao.primaryKeyFieldName
        }: _*)
        if (count == 0) defaultDao.createWithAttributes(defaultDao.toNamedValues(entity): _*)
        else if (count > 1) throw new IllegalStateException(s"$count entities are found for identifier: $entity.id")
      } else {
        defaultDao.createWithAttributes(defaultDao.toNamedValues(entity): _*)
      }
      (this.asInstanceOf[This], entity)
  }

  def deleteByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)] = withDBSession(ctx) {
    implicit s =>
      defaultDao.findBy(sqls.eq(defaultDao.defaultAlias.field(defaultDao.primaryKeyFieldName), identifier.value)).map {
        entity =>
          val count = defaultDao.deleteBy(sqls.eq(defaultDao.column.field(defaultDao.primaryKeyFieldName), identifier.value.toString))
          if (count == 1) (this.asInstanceOf[This], entity)
          else if (count > 1) throw new IllegalStateException(s"$count entities are found for identifier: $identifier")
          else throw RepositoryIOException(s"Entity (identifier: $identifier) is not found when deleting")
      }.getOrElse(throw RepositoryIOException(s"Entity (identifier: $identifier) is not found"))
  }

}
