package com.github.j5ik2o.spetstore.domain.lifecycle.customer

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityNotFoundException, RepositoryOnJDBC, EntityIOContext}
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer._
import java.util.UUID
import scala.util.Try
import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile
import com.github.j5ik2o.spetstore.domain.model.item.CategoryId
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.customer.Customer
import scalikejdbc.WrappedResultSet
import com.github.j5ik2o.spetstore.domain.model.basic.Contact

private[customer]
class CustomerRepositoryOnJDBC
  extends RepositoryOnJDBC[CustomerId, Customer] with CustomerRepository {

  def createDao = new Dao

  class Dao extends AbstractDao[Customer] {

    override def defaultAlias = createAlias("c")

    override val tableName = "customer"

    override def extract(resultSet: WrappedResultSet, c: ResultName[Customer]): Customer = Customer(
      id = CustomerId(UUID.fromString(resultSet.get(c.id))),
      name = resultSet.get(c.name),
      sexType = SexType(resultSet.get(c.sexType)),
      status = CustomerStatus(resultSet.get(c.status)),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          zipCode = ZipCode(resultSet.get(c.field("zipCode"))),
          pref = Pref(resultSet.int(c.field("prefCode"))),
          cityName = resultSet.string(c.field("cityName")),
          addressName = resultSet.string(c.field("addressName")),
          buildingName = resultSet.stringOpt(c.field("buildingName"))
        ),
        contact = Contact(
          email = resultSet.string(c.field("email")),
          phone = resultSet.string(c.field("phone"))
        )
      ),
      config = CustomerConfig(
        loginName = resultSet.string(c.field("loginName")),
        password = resultSet.string(c.field("password")),
        favoriteCategoryId = resultSet.stringOpt(c.field("favoriteCategoryId")).
          map(e => CategoryId(UUID.fromString(e)))
      )
    )

    override def toNamedValues(entity: Customer): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id.value.toString,
      'status -> entity.status.id,
      'name -> entity.name,
      'sexType -> entity.sexType.id,
      'zipCode -> entity.profile.postalAddress.zipCode.asString,
      'prefCode -> entity.profile.postalAddress.pref.id,
      'cityName -> entity.profile.postalAddress.cityName,
      'addressName -> entity.profile.postalAddress.addressName,
      'buildingName -> entity.profile.postalAddress.buildingName,
      'email -> entity.profile.contact.email,
      'phone -> entity.profile.contact.phone,
      'loginName -> entity.config.loginName,
      'password -> entity.config.password,
      'favoriteCategoryId -> entity.config.favoriteCategoryId.map(_.value.toString)
    )
  }

  def resolveByLoginName(loginName: String)(implicit ctx: EntityIOContext): Try[Option[Customer]] = withDBSession(ctx) {
    implicit s =>
      val c = defaultDao.defaultAlias
      defaultDao.findBy(sqls.eq(c.column("loginName"), loginName))
  }


}
