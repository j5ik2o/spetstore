package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import java.util.UUID
import com.github.j5ik2o.spetstore.domain.model.basic.{Pref, Contact, ZipCode, PostalAddress}
import com.github.j5ik2o.spetstore.domain.model.item.{Supplier, SupplierId}
import scalikejdbc._, SQLInterpolation._

private[item]
class SupplierRepositoryOnJDBC
  extends RepositoryOnJDBC[SupplierId, Supplier] with SupplierRepository {

  class Dao extends AbstractDao[Supplier] {

    override def defaultAlias = createAlias("s")

    override def tableName: String = "supplier"

    def toNamedValues(entity: Supplier): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id.value,
      'name -> entity.name,
      'zipCode -> entity.postalAddress.zipCode.asString,
      'prefCode -> entity.postalAddress.pref.id,
      'cityName -> entity.postalAddress.cityName,
      'addressName -> entity.postalAddress.addressName,
      'buildingName -> entity.postalAddress.buildingName,
      'email -> entity.contact.email,
      'phone -> entity.contact.phone
    )

    def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Supplier]): Supplier = {
      Supplier(
        id = SupplierId(UUID.fromString(rs.get(n.id))),
        name = rs.get(n.name),
        postalAddress = PostalAddress(
          zipCode = ZipCode(rs.get(n.field("zipCode"))),
          pref = Pref(rs.get(n.field("prefCode"))),
          cityName = rs.get(n.field("cityName")),
          addressName = rs.get(n.field("addressName")),
          buildingName = rs.get(n.field("buildingName"))
        ),
        contact = Contact(
          email = rs.get(n.field("email")),
          phone = rs.get(n.field("phone"))
        )
      )
    }

  }

  override protected def createDao = new Dao

}
