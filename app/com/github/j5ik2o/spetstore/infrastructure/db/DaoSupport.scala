package com.github.j5ik2o.spetstore.infrastructure.db

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityNotFoundException, RepositoryIOException }
import scalikejdbc._

import scala.util.Try

/**
 * データアクセスをサポートするためのトレイト。
 *
 * @tparam T テーブル型
 */
trait DaoSupport[T] {

  protected val mapper: CRUDMapper[T]

  private lazy val m = mapper.defaultAlias

  protected val idName: String = "id"

  def insertOrUpdate(id: Long, version: Option[Long], record: T)(implicit s: DBSession): Try[T] = Try {
    if (version.isDefined) {
      val where = sqls.eq(mapper.column.field(idName), id).and.eq(mapper.column.field("version"), version)
      println(where)
      val count = mapper.updateBy(where)
        .withAttributes(mapper.toNamedValues(record).filterNot {
          case (k, _) => k.name == mapper.primaryKeyFieldName
        }: _*)
      if (count == 0) {
        throw new IllegalStateException(s"The entity has illegal version(id = $id)")
      }
      findById(id).get
    } else {
      val result = mapper.updateById(id).withAttributes(mapper.toNamedValues(record): _*)
      if (result == 0) {
        mapper.createWithAttributes(mapper.toNamedValues(record): _*)
      }
      findById(id).get
    }
  }

  def findById(id: Long)(implicit s: DBSession): Try[T] = Try {
    mapper.findBy(sqls.eq(mapper.defaultAlias.field(idName), id)).getOrElse(throw new EntityNotFoundException(s"$id"))
  }

  def findAllWithLimitOffset(limit: Int, offset: Int)(implicit s: DBSession): Try[Seq[T]] = Try {
    mapper.findAllWithLimitOffset(limit, offset)
  }

  def deleteById(id: Long)(implicit s: DBSession): Try[T] = findById(id).map {
    entity =>
      if (mapper.deleteBy(sqls.eq(mapper.defaultAlias.field(idName), id)) == 0) {
        throw new RepositoryIOException("")
      } else {
        entity
      }
  }

}
