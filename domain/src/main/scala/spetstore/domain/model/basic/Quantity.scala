package spetstore.domain.model.basic

import cats.Monoid

case class Quantity(breachEncapsulationOfValue: Long) extends Ordered[Quantity] {
  override def compare(that: Quantity): Int = breachEncapsulationOfValue compare that.breachEncapsulationOfValue
}

object Quantity {

  val Zero: Quantity = Quantity(0L)
  val One: Quantity  = Quantity(1L)

  implicit val monoid: Monoid[Quantity] = new Monoid[Quantity] {
    override def empty: Quantity = Zero

    override def combine(x: Quantity, y: Quantity): Quantity =
      Quantity(x.breachEncapsulationOfValue + y.breachEncapsulationOfValue)
  }

}
