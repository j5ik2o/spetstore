package spetstore.domain.model.item

case class ItemName(breachEncapsulationOfValue: String) {
  require(breachEncapsulationOfValue.length < 255)
}
