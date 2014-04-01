package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContextOnMemory
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer._
import com.github.j5ik2o.spetstore.domain.model.item._
import org.specs2.mutable.Specification
import scala.util.Success
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

class CartSpec extends Specification {

  val identifierService = IdentifierService()

  "cart" should {
    val customer = Customer(
      id = CustomerId(identifierService.generate),
      status = StatusType.Enabled,
      name = "Junichi Kato",
      sexType = SexType.Female,
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          ZipCode("100", "1000"),
          Pref.東京都,
          "目黒区下目黒",
          "1-1-1"
        ),
        contact = Contact("hoge@hoge.com", "00-0000-0000")
      ),
      config = CustomerConfig(
        loginName = "fugafuga",
        password = "hogehoge",
        favoriteCategoryId = None
      )
    )
    val item = Item(
      id = ItemId(identifierService.generate),
      status = StatusType.Enabled,
      itemTypeId = ItemTypeId(identifierService.generate),
      name = "ぽち",
      description = None,
      price = BigDecimal(100),
      supplierId = SupplierId(identifierService.generate)
    )
    implicit val ctx = EntityIOContextOnMemory
    implicit val itemRepository = ItemRepository.ofMemory().storeEntity(item).get._1

    "add cartItem" in {
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List.empty
      )
      val cartItem = CartItem(CartItemId(identifierService.generate), 1, StatusType.Enabled, item.id, 1, false)
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(cartItem) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem as item" in {
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List.empty
      )
      val cartItemId = CartItemId(identifierService.generate)
      val newCart = cart.addCartItem(cartItemId, item, 1, false)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem if contains cartItem" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cartItem = CartItem(cartItemId, 1, StatusType.Enabled, item.id, 2, false)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(cartItem)
      )
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 4, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 4
    }
    "remove cartItem by itemId" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false))
      )
      val newCart = cart.removeCartItemByPetId(item.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.itemId == item.id && e.quantity == 1) must beFalse
      newCart.sizeOfCartItems must_== 0
      newCart.quantityOfCartItems must_== 0
    }
    "increment quantity by itemId" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false))
      )
      val newCart = cart.incrementQuantityByItemId(item.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.itemId == item.id && e.quantity == 2) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "update quantity by itemId" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false))
      )
      val newCart = cart.updateQuantityByItemId(item.id, 2)
      newCart must_== cart
      newCart.cartItems.exists(e => e.itemId == item.id && e.quantity == 2) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "contains item" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false))
      )
      cart.containsItemId(item.id) must beTrue
    }
    "get totalPrice" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false))
      )
      cart.totalPrice must_== Success(BigDecimal(100))
    }
    "get customer" in {
      val cartItemId = CartItemId(identifierService.generate)
      val cart = Cart(
        id = CartId(identifierService.generate),
        status = StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(CartItem(cartItemId, 1, StatusType.Enabled, item.id, 1, false))
      )
      implicit val ar = CustomerRepository.ofMemory(Map(customer.id -> customer))
      implicit val ctx = EntityIOContextOnMemory
      cart.customer.get must_== customer
    }
  }

}
