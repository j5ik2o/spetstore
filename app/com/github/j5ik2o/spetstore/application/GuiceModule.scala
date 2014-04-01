package com.github.j5ik2o.spetstore.application

import com.github.j5ik2o.spetstore.application.service.AuthenticationService
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.lifecycle.item._
import com.github.j5ik2o.spetstore.domain.lifecycle.purchase.{OrderRepository, CartRepository}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import scalikejdbc._
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

class GuiceModule extends AbstractModule with ScalaModule {

  def configure() {

    bind[IdentifierService].toInstance(IdentifierService())

    // application
    bind[EntityIOContextProvider].toInstance(new EntityIOContextProvider.JDBC(AutoSession))
    bind[AuthenticationService]

    // domain
    // -> customer
    bind[CustomerRepository].toInstance(CustomerRepository.ofJDBC)
    // -> category
    bind[CategoryRepository].toInstance(CategoryRepository.ofJDBC)

    // -> item
    bind[ItemTypeRepository].toInstance(ItemTypeRepository.ofMemory())
    bind[ItemRepository].toInstance(ItemRepository.ofMemory())
    bind[InventoryRepository].toInstance(InventoryRepository.ofMemory())
    bind[SupplierRepository].toInstance(SupplierRepository.ofMemory())
    // -> purchase
    bind[CartRepository].toInstance(CartRepository.ofMemory())
    bind[OrderRepository].toInstance(OrderRepository.ofMemory())


  }

}
