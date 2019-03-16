package spetstore.domain.model.basic

import cats.kernel.Semigroup

case class Quantity(breachEncapsulationOfValue: Long) extends Ordered[Quantity] {
  require(breachEncapsulationOfValue > 1 && breachEncapsulationOfValue <= 255)
  override def compare(that: Quantity): Int = breachEncapsulationOfValue compare that.breachEncapsulationOfValue
}

object Quantity {

  val One: Quantity  = Quantity(1L)

  implicit val semigroupQuantity: Semigroup[Quantity] = new Semigroup[Quantity] {

    override def combine(x: Quantity, y: Quantity): Quantity =
      Quantity(x.breachEncapsulationOfValue + y.breachEncapsulationOfValue)
  }

}
