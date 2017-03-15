package com.github.j5ik2o.spetstore.domain.basic

import org.joda.time.DateTime

object CardType extends Enumeration {
  val Visa, Master, UC, AmericanExpress = Value
}

case class CreditCard(
  cardNumber: String,
  expiryDate: DateTime,
  cardType: CardType.Value
)

