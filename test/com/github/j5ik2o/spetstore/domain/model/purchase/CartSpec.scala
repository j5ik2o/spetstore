package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContextOnMemory
import com.github.j5ik2o.spetstore.domain.model.basic.Contact
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer.Customer
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile
import com.github.j5ik2o.spetstore.domain.model.customer._
import com.github.j5ik2o.spetstore.domain.model.item.Item
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.item.ItemTypeId
import com.github.j5ik2o.spetstore.domain.model.item._
import org.specs2.mutable.Specification
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository

class CartSpec extends Specification {

  "cart" should {
    val customer = Customer(
      id = CustomerId(),
      status = CustomerStatus.Enabled,
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
      id = ItemId(),
      itemTypeId = ItemTypeId(),
      name = "ぽち",
      description = None,
      price = BigDecimal(100),
      supplierId = SupplierId()
    )
    "add cartItem" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List.empty
      )
      val cartItem = CartItem(item, 1, false)
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(cartItem) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem as item" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List.empty
      )
      val newCart = cart.addCartItem(item, 1, false)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(item, 1, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem if contains cartItem" in {
      val cartItem = CartItem(item, 2, false)
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(cartItem)
      )
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(item, 4, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 4
    }
    "remove cartItem by itemId" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(item, 1, false))
      )
      val newCart = cart.removeCartItemByPetId(item.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.item == item && e.quantity == 1) must beFalse
      newCart.sizeOfCartItems must_== 0
      newCart.quantityOfCartItems must_== 0
    }
    "increment quantity by itemId" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(item, 1, false))
      )
      val newCart = cart.incrementQuantityByItemId(item.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.item == item && e.quantity == 2) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "update quantity by itemId" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(item, 1, false))
      )
      val newCart = cart.updateQuantityByItemId(item.id, 2)
      newCart must_== cart
      newCart.cartItems.exists(e => e.item == item && e.quantity == 2) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "contains item" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(item, 1, false))
      )
      cart.containsItemId(item.id) must beTrue
    }
    "get totalPrice" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(item, 1, false))
      )
      cart.totalPrice must_== BigDecimal(100)
    }
    "get customer" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(item, 1, false))
      )
      implicit val ar = CustomerRepository.ofMemory(Map(customer.id -> customer))
      implicit val ctx = EntityIOContextOnMemory
      cart.customer.get must_== customer
    }
  }

}
