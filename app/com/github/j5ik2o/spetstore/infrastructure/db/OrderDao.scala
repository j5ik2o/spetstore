package com.github.j5ik2o.spetstore.infrastructure.db

import com.github.j5ik2o.spetstore.domain.support.support.EntityNotFoundException
import scalikejdbc._

import scala.util.Try

object OrderDao
    extends DaoSupport[OrderRecord] {

  override protected val mapper = OrderRecord

  override def findById(id: Long)(implicit s: DBSession): Try[OrderRecord] = Try {
    OrderRecord.joins(OrderRecord.orderItemsRef).
      findBy(sqls.eq(mapper.defaultAlias.field(idName), id)).
      getOrElse(throw new EntityNotFoundException(s"$id"))
  }

  override def findAllWithLimitOffset(limit: Int, offset: Int)(implicit s: DBSession): Try[Seq[OrderRecord]] = Try {
    OrderRecord.joins(OrderRecord.orderItemsRef).findAllWithLimitOffset(limit, offset)
  }

}

