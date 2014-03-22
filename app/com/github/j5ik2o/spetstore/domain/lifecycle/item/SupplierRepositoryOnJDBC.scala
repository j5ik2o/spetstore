package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.{SQLInterpolation, WrappedResultSet}
import java.util.UUID
import com.github.j5ik2o.spetstore.domain.model.basic.{Pref, Contact, ZipCode, PostalAddress}
import com.github.j5ik2o.spetstore.domain.model.item.{Supplier, SupplierId}

private[item]
class SupplierRepositoryOnJDBC
  extends RepositoryOnJDBC[SupplierId, Supplier] with SupplierRepository {

  override def tableName: String = "supplier"

  override def columnNames: Seq[String] = Seq(
    "id",
    "name",
    "status",
    "zip_code",
    "pref_code",
    "city_name",
    "address_name",
    "building_name",
    "email",
    "phone"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Supplier =
    Supplier(
      id = SupplierId(UUID.fromString(resultSet.string("id"))),
      name = resultSet.string("name"),
      postalAddress = PostalAddress(
        zipCode = ZipCode(resultSet.string("zip_code")),
        pref = Pref(resultSet.int("pref_code")),
        cityName = resultSet.string("city_name"),
        addressName = resultSet.string("address_name"),
        buildingName = resultSet.stringOpt("building_name")
      ),
      contact = Contact(
        email = resultSet.string("email"),
        phone = resultSet.string("phone")
      )
    )

  protected def convertEntityToValues(entity: Supplier): Seq[Any] = Seq(
    entity.id.value.toString,
    entity.name,
    entity.postalAddress.zipCode.asString,
    entity.postalAddress.pref.id,
    entity.postalAddress.cityName,
    entity.postalAddress.addressName,
    entity.postalAddress.buildingName,
    entity.contact.email,
    entity.contact.phone
  )

  protected def toNamedValues(entity: Supplier): Seq[(Symbol, Any)] = ???

  def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Supplier]): Supplier = ???
}
