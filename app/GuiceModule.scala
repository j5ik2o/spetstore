import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.service.AuthenticationService
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.lifecycle.item._
import com.github.j5ik2o.spetstore.domain.lifecycle.purchase.{ CartRepository, OrderRepository }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import scalikejdbc._

class GuiceModule extends AbstractModule with ScalaModule {

  def configure() {
    bind[IdentifierService].toInstance(IdentifierService())

    // application
    bind[EntityIOContextProvider].toInstance(new EntityIOContextProvider.JDBC(AutoSession))
    bind[AuthenticationService]

    bind[CustomerRepository].toInstance(CustomerRepository.ofJDBC)
    bind[CategoryRepository].toInstance(CategoryRepository.ofJDBC)
    bind[ItemRepository].toInstance(ItemRepository.ofJDBC)
    bind[CartRepository].toInstance(CartRepository.ofJDBC)

    bind[ItemTypeRepository].toInstance(ItemTypeRepository.ofJDBC)
    bind[InventoryRepository].toInstance(InventoryRepository.ofJDBC)
    bind[SupplierRepository].toInstance(SupplierRepository.ofJDBC)
    bind[OrderRepository].toInstance(OrderRepository.ofJDBC)

  }

}
