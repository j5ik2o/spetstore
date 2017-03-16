package com.github.j5ik2o.spetstore.domain.support.support

import com.github.j5ik2o.spetstore.infrastructure.db.{ DaoSupport, CRUDMapper }
import play.Logger
import scala.util.Try
import scalikejdbc._

/**
 * JDBC用[[com.github.j5ik2o.spetstore.domain.support.support.EntityIOContext]]。
 *
 * @param session DBセッション
 */
case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

trait SimpleRepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]] extends RepositoryOnJDBC[ID, E] {
  self =>

  private object Dao extends DaoSupport[T] {
    override protected lazy val mapper = self.mapper

  }

  protected def convertToEntity(record: T): E

  protected def convertToRecord(entity: E): T

  override def resolveById(identifier: ID)(implicit ctx: Ctx): Try[E] = withDBSession(ctx) {
    implicit s =>
      Dao.findById(identifier.value).map(convertToEntity)
  }

  override def store(entity: E)(implicit ctx: Ctx): Try[(This, E)] = withDBSession(ctx) {
    implicit s =>
      Dao.insertOrUpdate(entity.id.value, entity.version, convertToRecord(entity)).map {
        record =>
          (this.asInstanceOf[This], convertToEntity(record))
      }
  }

  override def deleteById(identifier: ID)(implicit ctx: Ctx): Try[(This, E)] = withDBSession(ctx) {
    implicit s =>
      Dao.deleteById(identifier.value).map {
        record =>
          (this.asInstanceOf[This], convertToEntity(record))
      }
  }

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[E]] = withDBSession(ctx) {
    implicit s =>
      Dao.findAllWithLimitOffset(limit, offset).map(_.map(convertToEntity))
  }

}

/**
 * JDBC用リポジトリのための骨格実装。
 */
abstract class RepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]]
    extends Repository[ID, E] with MultiIOSupport[ID, E] {

  type T

  protected val mapper: CRUDMapper[T]

  protected val idName: String = "id"

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): A = {
    ctx match {
      case EntityIOContextOnJDBC(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

  def existById(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      Try {
        val count = mapper.countBy(sqls.eq(mapper.defaultAlias.field(idName), identifier.value))
        if (count == 0) false
        else if (count == 1) true
        else throw new IllegalStateException(s"$count entities are found for identifier: $identifier")
      }
  }

  override def existByIds(identifiers: ID*)(implicit ctx: Ctx): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      Try(mapper.countBy(sqls.in(mapper.defaultAlias.field(idName), identifiers.map(_.value))) > 0)
  }

}
