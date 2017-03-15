package com.github.j5ik2o.spetstore.infrastructure.db

import com.github.j5ik2o.spetstore.domain.support.support.EntityNotFoundException
import scalikejdbc._

import scala.util.Try

object CartDao
    extends DaoSupport[CartRecord] {

  override protected val mapper = CartRecord

  override def findById(id: Long)(implicit s: DBSession): Try[CartRecord] = Try {
    CartRecord.joins(CartRecord.cartItemsRef).
      findBy(sqls.eq(mapper.defaultAlias.field(idName), id)).
      getOrElse(throw new EntityNotFoundException(s"$id"))
  }

  override def findAllWithLimitOffset(limit: Int, offset: Int)(implicit s: DBSession): Try[Seq[CartRecord]] = Try {
    CartRecord.joins(CartRecord.cartItemsRef).findAllWithLimitOffset(limit, offset)
  }

}

