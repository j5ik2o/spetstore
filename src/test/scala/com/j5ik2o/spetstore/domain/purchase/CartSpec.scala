package com.j5ik2o.spetstore.domain.purchase

import com.j5ik2o.spetstore.domain.account._
import com.j5ik2o.spetstore.domain.address.Contact
import com.j5ik2o.spetstore.domain.address.PostalAddress
import com.j5ik2o.spetstore.domain.address.Pref
import com.j5ik2o.spetstore.domain.address.ZipCode
import com.j5ik2o.spetstore.domain.item.{ItemTypeId, ItemId, Item}
import org.specs2.mutable.Specification
import com.j5ik2o.spetstore.infrastructure.support.EntityIOContextOnMemory

class CartSpec extends Specification {

  "cart" should {
    val account = Account(
      id = AccountId(),
      status = AccountStatus.Enabled,
      name = "Junichi Kato",
      profile = AccountProfile(
        postalAddress = PostalAddress(
          ZipCode("100", "1000"),
          Pref.東京都,
          "目黒区下目黒",
          "1-1-1"
        ),
        contact = Contact("hoge@hoge.com", "00-0000-0000")
      ),
      config = AccountConfig(
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
      quantity = 1
    )
    "add cartItem" in {
      val cart = Cart(
        id = CartId(),
        accountId = account.id,
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
        accountId = account.id,
        cartItems = List.empty
      )
      val newCart = cart.addCartItem(item, 1, false)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(item, 1, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem if contains cartItem" in {
      val cartItem = CartItem(item, 1, false)
      val cart = Cart(
        id = CartId(),
        accountId = account.id,
        cartItems = List(cartItem)
      )
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(item, 2, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "remove cartItem by itemId" in {
      val cart = Cart(
        id = CartId(),
        accountId = account.id,
        cartItems = List(CartItem(item, 1, false))
      )
      val newCart = cart.removeItemById(item.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.item == item && e.quantity == 1) must beFalse
      newCart.sizeOfCartItems must_== 0
      newCart.quantityOfCartItems must_== 0
    }
    "increment quantity by itemId" in {
      val cart = Cart(
        id = CartId(),
        accountId = account.id,
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
        accountId = account.id,
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
        accountId = account.id,
        cartItems = List(CartItem(item, 1, false))
      )
      cart.containsItemId(item.id) must beTrue
    }
    "get totalPrice" in {
      val cart = Cart(
        id = CartId(),
        accountId = account.id,
        cartItems = List(CartItem(item, 1, false))
      )
      cart.totalPrice must_== BigDecimal(100)
    }
    "get account" in {
      val cart = Cart(
        id = CartId(),
        accountId = account.id,
        cartItems = List(CartItem(item, 1, false))
      )
      implicit val ar = AccountRepository.ofMemory(Map(account.id -> account))
      implicit val ctx = EntityIOContextOnMemory
      cart.account.get must_== account
    }
  }

}
