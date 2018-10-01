package spetstore.domain.model.basic

import cats.Monoid
import org.sisioh.baseunits.scala.money.Money

case class Price(breachEncapsulationOfValue: Money) {

  def withNewPrice(value: Money): Price =
    copy(breachEncapsulationOfValue = value)

  def times(factor: Quantity): Price = copy(breachEncapsulationOfValue * BigDecimal(factor.breachEncapsulationOfValue))

  def *(factor: Quantity): Price = times(factor)

}

object Price {

  val Zero = Price(Money.yens(0))

  implicit val monoid: Monoid[Price] = new Monoid[Price] {
    override def empty: Price = Zero

    override def combine(x: Price, y: Price): Price =
      Price(x.breachEncapsulationOfValue + y.breachEncapsulationOfValue)
  }

}
