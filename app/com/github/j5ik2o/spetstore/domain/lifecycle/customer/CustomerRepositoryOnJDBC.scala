package com.github.j5ik2o.spetstore.domain.lifecycle.customer

import com.github.j5ik2o.spetstore.domain.support.support._
import com.github.j5ik2o.spetstore.domain.model.basic.Contact
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer.Customer
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile
import com.github.j5ik2o.spetstore.infrastructure.db.CustomerRecord
import scala.util.Try
import scalikejdbc._, SQLInterpolation._

private[customer] class CustomerRepositoryOnJDBC
    extends SimpleRepositoryOnJDBC[CustomerId, Customer]
    with CustomerRepository {

  type T = CustomerRecord

  override protected lazy val mapper = CustomerRecord

  protected def convertToEntity(record: CustomerRecord): Customer =
    Customer(
      id = CustomerId(record.id),
      status = StatusType(record.status),
      name = record.name,
      sexType = SexType(record.sexType),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          zipCode = ZipCode(record.zipCode),
          pref = Pref(record.prefCode),
          cityName = record.cityName,
          addressName = record.addressName,
          buildingName = record.buildingName
        ),
        contact = Contact(
          email = record.email,
          phone = record.phone
        )
      ),
      config = CustomerConfig(
        loginName = record.loginName,
        password = record.password
      ),
      version = Some(record.version)
    )

  override protected def convertToRecord(entity: Customer) = CustomerRecord(
    id = entity.id.value,
    status = entity.status.id,
    name = entity.name,
    sexType = entity.sexType.id,
    zipCode = entity.profile.postalAddress.zipCode.asString,
    prefCode = entity.profile.postalAddress.pref.id,
    cityName = entity.profile.postalAddress.cityName,
    addressName = entity.profile.postalAddress.addressName,
    buildingName = entity.profile.postalAddress.buildingName,
    email = entity.profile.contact.email,
    phone = entity.profile.contact.phone,
    loginName = entity.config.loginName,
    password = entity.config.password,
    favoriteCategoryId = entity.config.favoriteCategoryId.map(_.value),
    version = entity.version.getOrElse(1)
  )

  def resolveByLoginName(loginName: String)(implicit ctx: EntityIOContext): Try[Option[Customer]] = withDBSession(ctx) {
    implicit s =>
      Try {
        val c = mapper.defaultAlias
        mapper.findBy(sqls.eq(c.loginName, loginName)).map(convertToEntity)
      }
  }

}
