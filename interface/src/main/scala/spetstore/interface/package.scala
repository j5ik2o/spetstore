package spetstore

import monix.eval.Task
import org.hashids.Hashids
import spetstore.domain.model.UserAccountId
import spetstore.domain.model.item.ItemId
import spetstore.interface.api.controller.{ ItemController, UserAccountController }
import spetstore.interface.api.{ ApiServer, Routes, SwaggerDocService }
import spetstore.interface.generator.{ ItemIdGeneratorOnJDBC, UserAccountIdGeneratorOnJDBC }
import spetstore.interface.repository.{ ItemRepositoryOnJDBC, UserAccountRepositoryOnJDBC }
import spetstore.useCase.port.generator.IdGenerator
import spetstore.useCase.port.repository.{ ItemRepository, UserAccountRepository }
import wvlet.airframe._

package object interface {

  def createInterfaceDesign(host: String, port: Int, hashidsSalt: String, apiClasses: Set[Class[_]]): Design =
    newDesign
      .bind[Hashids].toInstance(new Hashids(hashidsSalt))
      .bind[Routes].toSingleton
      .bind[SwaggerDocService].toInstance(new SwaggerDocService(host, port, apiClasses))
      .bind[ApiServer].toSingleton
      .bind[UserAccountRepository[Task]].to[UserAccountRepositoryOnJDBC]
      .bind[IdGenerator[UserAccountId]].to[UserAccountIdGeneratorOnJDBC]
      .bind[UserAccountController].toSingleton
      .bind[ItemRepository[Task]].to[ItemRepositoryOnJDBC]
      .bind[IdGenerator[ItemId]].to[ItemIdGeneratorOnJDBC]
      .bind[ItemController].toSingleton
      .add(useCase.createUseCaseDesign)

}
