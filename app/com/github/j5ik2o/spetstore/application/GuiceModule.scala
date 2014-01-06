package com.github.j5ik2o.spetstore.application

import com.github.j5ik2o.spetstore.application.service.AuthenticationService
import com.github.j5ik2o.spetstore.domain.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.pet.{CategoryRepository, PetTypeRepository, PetRepository}
import com.github.j5ik2o.spetstore.domain.purchase.{OrderRepository, CartRepository}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class GuiceModule extends AbstractModule with ScalaModule {

  def configure() {
    bind[CustomerRepository].toInstance(CustomerRepository.ofMemory())

    bind[CategoryRepository].toInstance(CategoryRepository.ofMemory())
    bind[PetTypeRepository].toInstance(PetTypeRepository.ofMemory())
    bind[PetRepository].toInstance(PetRepository.ofMemory())

    bind[CartRepository].toInstance(CartRepository.ofMemory())
    bind[OrderRepository].toInstance(OrderRepository.ofMemory())

    bind[EntityIOContextProvider].toInstance(EntityIOContextProvider.ofMemory)

    bind[AuthenticationService]

  }

}
