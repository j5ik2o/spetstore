package spetstore.domain.model.customer

import com.github.j5ik2o.dddbase.{Aggregate, AggregateLongId}
import org.sisioh.baseunits.scala.time.TimePoint
import spetstore.domain.model.basic.{Contact, PostalAddress, StatusType}

import scala.reflect.{ClassTag, classTag}

case class CustomerId(value: Long) extends AggregateLongId

case class Customer(id: CustomerId,
                    status: StatusType,
                    name: CustomerName,
                    postalAddress: PostalAddress,
                    contact: Contact,
                    createdAt: TimePoint,
                    updatedAt: Option[TimePoint])
    extends Aggregate {
  override type IdType        = CustomerId
  override type AggregateType = Customer
  override protected val tag: ClassTag[Customer] = classTag[Customer]
}
