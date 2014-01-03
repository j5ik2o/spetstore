package com.j5ik2o.spetstore.infrastructure.support

import scala.util.Try
import scalikejdbc._, SQLInterpolation._

case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

abstract class RepositoryOnJDBC[ID <: Identifier[_], E <: Entity[ID]]
  extends Repository[ID, E] with SQLSyntaxSupport[E]{

  protected def convertToEntity(resultSet: WrappedResultSet): E

  protected def convertToValues(entity: E): Seq[Any]

  protected def convertToKeyValues(entity: E): Map[String, Any] =
    columnNames.zip(convertToValues(entity)).toMap

  private def getDBSession(ctx: EntityIOContext): DBSession = ctx match {
    case EntityIOContextOnJDBC(dbSession) => dbSession
    case _ => throw new IllegalArgumentException(s"ctx is $ctx")
  }

  def contains(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean] = Try {
    implicit val dbSession = getDBSession(ctx)
    sql"select count(id) from $table where id = ?".
      bind(identifier.value.toString).
      map(_.int(1) == 1).single().apply().
      getOrElse(false)
  }

  def resolve(identifier: ID)(implicit ctx: EntityIOContext): Try[E] = Try {
    implicit val dbSession = getDBSession(ctx)
    sql"select * from $table where id = ?".
      bind(identifier.value.toString).
      map(convertToEntity).single().apply().
      getOrElse(throw EntityNotFoundException(identifier))
  }

  def store(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)] = Try {
    implicit val dbSession = getDBSession(ctx)
    def update(entity: E) = {
      val usb = new UpdateSQLBuilder(sqls"update $table")
      val kv = convertToKeyValues(entity).map {
        case (k, v) => (column.column(k), v)
      }
      usb.set(kv.toList: _*).toSQL.update().apply()
    }
    def insert(entity: E) = {
      val isb = new InsertSQLBuilder(sqls"insert into $table")
      val columns = columnNames.map(column.column)
      isb.columns(columns.toList: _*).values(convertToValues(entity): _*).toSQL.update().apply()
    }
    if (update(entity) > 0) {
      (this.asInstanceOf[This], entity)
    } else {
      if (insert(entity) > 0) {
        (this.asInstanceOf[This], entity)
      } else {
        throw RepositoryIOException("store error")
      }
    }
  }

  def delete(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)] = {
    implicit val dbSession = getDBSession(ctx)
    resolve(identifier).map {
      entity =>
        if (sql"delete from $table where id = ?".bind(identifier.value.toString).update().apply() > 0) {
          (this.asInstanceOf[This], entity)
        } else {
          throw RepositoryIOException("delete error")
        }
    }
  }

}
