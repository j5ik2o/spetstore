package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.purchase.{Order, OrderId}
import com.github.j5ik2o.spetstore.infrastructure.db.OrderRecord
import scala.util.Try

class OrderRepositoryOnJDBC
  extends RepositoryOnJDBC[OrderId, Order] with OrderRepository {

  override type T = OrderRecord

  override protected val mapper = OrderRecord

  override def deleteByIdentifier(identifier: OrderId)(implicit ctx: OrderRepositoryOnJDBC#Ctx): Try[(OrderRepositoryOnJDBC#This, Order)] = ???

  override def storeEntity(entity: Order)(implicit ctx: OrderRepositoryOnJDBC#Ctx): Try[(OrderRepositoryOnJDBC#This, Order)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: EntityIOContext): Try[Seq[Order]] = ???

  override def resolveEntity(identifier: OrderId)(implicit ctx: OrderRepositoryOnJDBC#Ctx): Try[Order] = ???
}
