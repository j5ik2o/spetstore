package spetstore.interface.api.model

case class CartItemBody(id: String, no: Int, itemId: String, quantity: Long, inStock: Boolean)

case class ResolveCartResponseBody(id: String,
                                   userAccountId: String,
                                   cartItems: Seq[CartItemBody],
                                   createdAt: Long,
                                   updatedAt: Option[Long])
