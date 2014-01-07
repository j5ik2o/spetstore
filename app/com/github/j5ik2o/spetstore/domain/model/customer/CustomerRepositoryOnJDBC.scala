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

  // must be `def`
  override val connectionPoolName = 'default
  override val tableName = "customer"

  override def extract(resultSet: WrappedResultSet, c: ResultName[Customer]): Customer = Customer(
    id = CustomerId(UUID.fromString(resultSet.string("id"))),
    name = resultSet.string("name"),
    sexType = resultSet.intOpt("sex_type").map(SexType(_)),
    status = CustomerStatus(resultSet.int("status")),
    profile = CustomerProfile(
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
    ),
    config = CustomerConfig(
      loginName = resultSet.string("login_name"),
      password = resultSet.string("password"),
      favoriteCategoryId = resultSet.stringOpt("favorite_category_id").
        map(e => CategoryId(UUID.fromString(e)))
    )
  )

  override def toNamedValues(entity: Customer): Seq[(Symbol, Any)] = Seq(
    'id -> entity.id.value.toString,
    'name -> entity.name,
    'status -> entity.status.id,
    'zipCode -> entity.profile.postalAddress.zipCode.asString,
    'pref -> entity.profile.postalAddress.pref.id,
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
