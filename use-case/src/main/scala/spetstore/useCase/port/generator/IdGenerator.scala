package spetstore.useCase.port.generator

import monix.eval.Task

trait IdGenerator[+ID] {

  def generateId(): Task[ID]

}
