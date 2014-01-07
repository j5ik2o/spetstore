package com.github.j5ik2o.spetstore.application

import com.github.j5ik2o.spetstore.application.service.AuthenticationService
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.model.pet._
import com.github.j5ik2o.spetstore.domain.model.purchase.{OrderRepository, CartRepository}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class GuiceModule extends AbstractModule with ScalaModule {

  def configure() {
    // application
    bind[EntityIOContextProvider].toInstance(EntityIOContextProvider.ofMemory)
    bind[AuthenticationService]

    // domain
    // -> customer
    bind[CustomerRepository].toInstance(CustomerRepository.ofMemory())
    // -> pet
    bind[CategoryRepository].toInstance(CategoryRepository.ofMemory())
    bind[PetTypeRepository].toInstance(PetTypeRepository.ofMemory())
    bind[PetRepository].toInstance(PetRepository.ofMemory())
    bind[InventoryRepository].toInstance(InventoryRepository.ofMemory())
    bind[SupplierRepository].toInstance(SupplierRepository.ofMemory())
    // -> purchase
    bind[CartRepository].toInstance(CartRepository.ofMemory())
    bind[OrderRepository].toInstance(OrderRepository.ofMemory())


  }

}
