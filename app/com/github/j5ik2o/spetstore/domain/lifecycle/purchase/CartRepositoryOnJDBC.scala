package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.purchase.{CartItem, Cart, CartId}
import java.util.UUID
import com.github.j5ik2o.spetstore.domain.infrastructure.json.CartFormats._
import org.json4s.DefaultReaders._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scalikejdbc._, SQLInterpolation._
import scala.util.Try
import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.db.CartRecord


private[purchase]
class CartRepositoryOnJDBC
  extends RepositoryOnJDBC[CartId, Cart] with CartRepository {

  override type T = CartRecord

  override protected val mapper = CartRecord

  override def deleteByIdentifier(identifier: CartId)(implicit ctx: CartRepositoryOnJDBC#Ctx): Try[(CartRepositoryOnJDBC#This, Cart)] = ???

  override def storeEntity(entity: Cart)(implicit ctx: CartRepositoryOnJDBC#Ctx): Try[(CartRepositoryOnJDBC#This, Cart)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: EntityIOContext): Try[Seq[Cart]] = ???

  override def resolveEntity(identifier: CartId)(implicit ctx: CartRepositoryOnJDBC#Ctx): Try[Cart] = ???
}
