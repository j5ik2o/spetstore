package com.github.j5ik2o.spetstore.infrastructure.db

import scalikejdbc._

import scala.util.Try

case class CartItemDao(cartId: Long)
    extends DaoSupport[CartItemRecord] {

  override protected val mapper = CartItemRecord

  def findByCartId: Try[Seq[CartItemRecord]] = Try {
    val alias = mapper.defaultAlias
    mapper.findAllBy(sqls.eq(alias.cartId, cartId))
  }

}

