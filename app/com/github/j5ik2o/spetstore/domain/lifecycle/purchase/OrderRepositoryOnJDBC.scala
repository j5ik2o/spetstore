package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.basic.{Pref, ZipCode}
import com.github.j5ik2o.spetstore.domain.model.purchase.{OrderStatus, OrderItem, Order, OrderId}
import java.util.UUID
import org.joda.time.DateTime
import org.json4s.DefaultReaders._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.domain.infrastructure.json.OrderFormats._
import scala.util.Try
import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.db.OrderRecord

class OrderRepositoryOnJDBC
  extends RepositoryOnJDBC[OrderId, Order] with OrderRepository {

  override type T = OrderRecord

  override protected val mapper = OrderRecord

  override def deleteByIdentifier(identifier: OrderId)(implicit ctx: OrderRepositoryOnJDBC#Ctx): Try[(OrderRepositoryOnJDBC#This, Order)] = ???

  override def storeEntity(entity: Order)(implicit ctx: OrderRepositoryOnJDBC#Ctx): Try[(OrderRepositoryOnJDBC#This, Order)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: EntityIOContext): Try[Seq[Order]] = ???

  override def resolveEntity(identifier: OrderId)(implicit ctx: OrderRepositoryOnJDBC#Ctx): Try[Order] = ???
}
