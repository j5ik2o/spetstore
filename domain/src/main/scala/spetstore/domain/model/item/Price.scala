package spetstore.domain.model.item

import org.sisioh.baseunits.scala.money.Money

case class Price(breachEncapsulationOfValue: Money) {
  def withNewPrice(value: Money): Price =
    copy(breachEncapsulationOfValue = value)
}
