package com.github.j5ik2o.spetstore.domain.model.customer

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityNotFoundException, RepositoryOnJDBC, EntityIOContext}
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.pet.CategoryId
import java.util.UUID
import scala.util.Try
import scalikejdbc._, SQLInterpolation._

private[customer]
class CustomerRepositoryOnJDBC
  extends RepositoryOnJDBC[CustomerId, Customer] with CustomerRepository {

  override def defaultAlias = createAlias("c")

  override val tableName = "customer"

  override def extract(resultSet: WrappedResultSet, c: ResultName[Customer]): Customer = Customer(
    id = CustomerId(UUID.fromString(resultSet.get(c.id))),
    name = resultSet.get(c.name),
    sexType = resultSet.intOpt(c.sexType).map(SexType(_)),
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
    'sexType -> entity.sexType.map(_.id),
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


  def resolveByLoginName(loginName: String)(implicit ctx: EntityIOContext): Try[Customer] = ??? //withDBSession {
//    implicit s =>
//          ???
////      findBy(sqls.eq(defaultAlias.field(primaryKeyName).eq, identifier.value)).getOrElse(throw EntityNotFoundException(identifier))   sql"select * from $table where login_name = ?".bind(loginName).map(convertResultSetToEntity).
////      single().apply().getOrElse(throw EntityNotFoundException(s"loginName = $loginName"))
//  }

}
