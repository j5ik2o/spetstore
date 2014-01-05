package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.domain.customer._
import com.github.j5ik2o.spetstore.domain.address.Contact
import com.github.j5ik2o.spetstore.domain.address.PostalAddress
import com.github.j5ik2o.spetstore.domain.address.Pref
import com.github.j5ik2o.spetstore.domain.address.ZipCode
import com.github.j5ik2o.spetstore.domain.pet.{PetTypeId, PetId, Pet}
import org.specs2.mutable.Specification
import com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContextOnMemory

class CartSpec extends Specification {

  "cart" should {
    val customer = Customer(
      id = CustomerId(),
      status = CustomerStatus.Enabled,
      name = "Junichi Kato",
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
    val pet = Pet(
      id = PetId(),
      petTypeId = PetTypeId(),
      name = "ぽち",
      description = None,
      price = BigDecimal(100),
      quantity = 1
    )
    "add cartItem" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List.empty
      )
      val cartItem = CartItem(pet, 1, false)
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(cartItem) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem as pet" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List.empty
      )
      val newCart = cart.addCartItem(pet, 1, false)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(pet, 1, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 1
    }
    "add cartItem if contains cartItem" in {
      val cartItem = CartItem(pet, 2, false)
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(cartItem)
      )
      val newCart = cart.addCartItem(cartItem)
      newCart must_== cart
      newCart.cartItems.contains(CartItem(pet, 4, false)) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 4
    }
    "remove cartItem by itemId" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(pet, 1, false))
      )
      val newCart = cart.removeItemById(pet.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.pet == pet && e.quantity == 1) must beFalse
      newCart.sizeOfCartItems must_== 0
      newCart.quantityOfCartItems must_== 0
    }
    "increment quantity by itemId" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(pet, 1, false))
      )
      val newCart = cart.incrementQuantityByItemId(pet.id)
      newCart must_== cart
      newCart.cartItems.exists(e => e.pet == pet && e.quantity == 2) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "update quantity by itemId" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(pet, 1, false))
      )
      val newCart = cart.updateQuantityByItemId(pet.id, 2)
      newCart must_== cart
      newCart.cartItems.exists(e => e.pet == pet && e.quantity == 2) must beTrue
      newCart.sizeOfCartItems must_== 1
      newCart.quantityOfCartItems must_== 2
    }
    "contains pet" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(pet, 1, false))
      )
      cart.containsItemId(pet.id) must beTrue
    }
    "get totalPrice" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(pet, 1, false))
      )
      cart.totalPrice must_== BigDecimal(100)
    }
    "get customer" in {
      val cart = Cart(
        id = CartId(),
        customerId = customer.id,
        cartItems = List(CartItem(pet, 1, false))
      )
      implicit val ar = CustomerRepository.ofMemory(Map(customer.id -> customer))
      implicit val ctx = EntityIOContextOnMemory
      cart.customer.get must_== customer
    }
  }

}
