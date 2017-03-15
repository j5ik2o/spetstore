package com.github.j5ik2o.spetstore.infrastructure.db

import scalikejdbc._

import scala.util.Try

case class OrderItemDao(orderId: Long)
    extends DaoSupport[OrderItemRecord] {

  override protected val mapper = OrderItemRecord

  def findByOrderId: Try[Seq[OrderItemRecord]] = Try {
    val alias = mapper.defaultAlias
    mapper.findAllBy(sqls.eq(alias.orderId, orderId))
  }

}

