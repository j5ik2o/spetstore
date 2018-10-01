package spetstore.domain.model.item

case class ItemDescription(breachEncapsulationOfValue: String) {
  require(breachEncapsulationOfValue.length < 255)
}
