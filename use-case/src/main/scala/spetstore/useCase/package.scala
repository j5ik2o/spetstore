package spetstore

import wvlet.airframe._

package object useCase {

  def createUseCaseDesign: Design =
    newDesign.bind[UserAccountUseCase].toSingleton

}
