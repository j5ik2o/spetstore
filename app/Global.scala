import com.github.j5ik2o.spetstore.application.GuiceModule
import com.google.inject.Guice
import play.api._

object Global extends GlobalSettings {

  private lazy val injector = Guice.createInjector(new GuiceModule)

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }

}
