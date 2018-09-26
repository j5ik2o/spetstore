package spetstore.domain.model.basic

import enumeratum._

import scala.collection.immutable

sealed abstract class StatusType(override val entryName: String) extends EnumEntry

object StatusType extends Enum[StatusType] with CirceEnum[StatusType] {
  override def values: immutable.IndexedSeq[StatusType] = findValues

  case object Enabled extends StatusType("enabled")

  case object Suspended extends StatusType("suspended")

  case object Disabled extends StatusType("disabled")

}
