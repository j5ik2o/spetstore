import com.google.inject.AbstractModule
import spetstore.interface.repository.{ ItemRepositoryBySlick, ItemRepositoryTask }

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ItemRepositoryTask])
      .to(classOf[ItemRepositoryBySlick])
  }

}
