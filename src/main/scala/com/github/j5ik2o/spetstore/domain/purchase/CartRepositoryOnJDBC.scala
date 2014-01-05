package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.domain.account.AccountId
import com.github.j5ik2o.spetstore.domain.item.{ItemTypeId, ItemId, Item}
import com.github.j5ik2o.spetstore.infrastructure.support.{Identifier, RepositoryOnJDBC}
import java.util.UUID
import org.json4s.DefaultWriters._
import org.json4s.DefaultReaders._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scalikejdbc.WrappedResultSet

private[purchase]
class CartRepositoryOnJDBC
  extends RepositoryOnJDBC[CartId, Cart] with CartRepository {

  override def tableName: String = "cart"

  override def columnNames: Seq[String] = Seq(
    "id",
    "account_id",
    "cart_items"
  )

  case class IdentifierFormat[A <: Identifier[_]](apply: UUID => A)
    extends Writer[A] with Reader[A] {

    def write(obj: A): JValue = JString(obj.value.toString)

    def read(value: JValue): A = apply(UUID.fromString((value \ "id").as[String]))

  }

  implicit val itemIdFormat = IdentifierFormat(ItemId.apply)
  implicit val itemTypeIdFormat = IdentifierFormat(ItemTypeId.apply)

  implicit object ItemFormat extends Writer[Item] with Reader[Item] {
    def write(obj: Item): JValue =
      JObject(
        JField("id", obj.id.asJValue),
        JField("itemTypeId", obj.itemTypeId.asJValue),
        JField("name", JString(obj.name)),
        JField("description", obj.description.map(JString).getOrElse(JNull)),
        JField("price", JDecimal(obj.price)),
        JField("quantity", JInt(obj.quantity))
      )

    def read(value: JValue): Item = Item(
      id = (value \ "id").as[ItemId],
      itemTypeId = (value \ "itemTypeId").as[ItemTypeId],
      name = (value \ "name").as[String],
      description = (value \ "description").as[Option[String]],
      price = (value \ "price").as[BigDecimal],
      quantity = (value \ "quantity").as[Int]
    )
  }

  implicit object CartItemFormat extends Writer[CartItem] with Reader[CartItem] {

    def write(obj: CartItem): JValue =
      JObject(
        JField("item", obj.item.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("isInStock", JBool(obj.inStock))
      )

    def read(value: JValue): CartItem = CartItem(
      item = (value \ "item").as[Item],
      quantity = (value \ "quantity").as[Int],
      inStock = (value \ "isInStock").as[Boolean]
    )
  }


  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Cart =
    Cart(
      id = CartId(UUID.fromString(resultSet.string("id"))),
      accountId = AccountId(UUID.fromString(resultSet.string("account_id"))),
      cartItems = parse(resultSet.string("cart_items")).as[List[CartItem]]
    )

  protected def convertEntityToValues(entity: Cart): Seq[Any] = Seq(
    entity.id,
    entity.accountId.value.toString,
    entity.cartItems.toArray.asJValue
  )


}
