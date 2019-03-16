package spetstore.domain.model.purchase

import com.github.j5ik2o.dddbase.Aggregate
import org.sisioh.baseunits.scala.time.{CalendarDate, TimePoint}
import spetstore.domain.model.UserAccountId
import spetstore.domain.model.basic.{Contact, PostalAddress, StatusType}
import spetstore.domain.model.customer.CustomerName

import scala.reflect.{ClassTag, classTag}

case class Order(id: OrderId,
                 status: StatusType,
                 orderDate: CalendarDate,
                 userAccountId: UserAccountId,
                 customerName: CustomerName,
                 shippingAddress: PostalAddress,
                 shippingContact: Contact,
                 createdAt: TimePoint,
                 updatedAt: Option[TimePoint])
    extends Aggregate {
  override type IdType        = OrderId
  override type AggregateType = Order
  override protected val tag: ClassTag[Order] = classTag[Order]
}
