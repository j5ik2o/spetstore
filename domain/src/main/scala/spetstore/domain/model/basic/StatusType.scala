package spetstore.domain.model.basic

import enumeratum._

import scala.collection.immutable

sealed abstract class StatusType(override val entryName: String) extends EnumEntry

object StatusType extends Enum[StatusType] with CirceEnum[StatusType] {
  override def values: immutable.IndexedSeq[StatusType] = findValues

  case object Active extends StatusType("active")

  case object Suspended extends StatusType("suspended")

  case object Deleted extends StatusType("deleted")

}
