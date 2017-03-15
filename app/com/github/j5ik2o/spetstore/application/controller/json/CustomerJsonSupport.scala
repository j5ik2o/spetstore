package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.application.controller.CustomerController
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer.{ Customer, CustomerConfig, CustomerId, CustomerProfile }

/**
 * [[Customer]]のJSONを表現したモデル。
 *
 * @param id                 ID
 * @param name               名前
 * @param sexType            性別
 * @param zipCode1           郵便番号1
 * @param zipCode2           郵便番号2
 * @param prefCode           都道府県コード
 * @param cityName           市区町村名
 * @param addressName        地番名
 * @param buildingName       建物名
 * @param email              メールアドレス
 * @param phone              電話番号
 * @param loginName          ログイン名
 * @param password           パスワード
 * @param favoriteCategoryId お気に入りカテゴリID
 */
case class CustomerJson(
  id: Option[String],
  name: String,
  sexType: Int,
  zipCode1: String,
  zipCode2: String,
  prefCode: Int,
  cityName: String,
  addressName: String,
  buildingName: Option[String],
  email: String,
  phone: String,
  loginName: String,
  password: String,
  favoriteCategoryId: Option[String],
  version: Option[Long]
)

trait CustomerJsonSupport {
  this: CustomerController =>

  protected def convertToEntity(customerJson: CustomerJson): Customer =
    Customer(
      id = CustomerId(customerJson.id.map(_.toLong).get),
      status = StatusType.Enabled,
      name = customerJson.name,
      sexType = SexType(customerJson.sexType),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          ZipCode(customerJson.zipCode1, customerJson.zipCode2),
          Pref(customerJson.prefCode),
          customerJson.cityName,
          customerJson.addressName,
          customerJson.buildingName
        ),
        contact = Contact(customerJson.email, customerJson.phone)
      ),
      config = CustomerConfig(
        loginName = customerJson.loginName,
        password = customerJson.password,
        favoriteCategoryId = None
      ),
      version = customerJson.version
    )

  protected def convertToEntityWithoutId(customerJson: CustomerJson): Customer =
    Customer(
      id = CustomerId(identifierService.generate),
      status = StatusType.Enabled,
      name = customerJson.name,
      sexType = SexType(customerJson.sexType),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          ZipCode(customerJson.zipCode1, customerJson.zipCode2),
          Pref(customerJson.prefCode),
          customerJson.cityName,
          customerJson.addressName,
          customerJson.buildingName
        ),
        contact = Contact(customerJson.email, customerJson.phone)
      ),
      config = CustomerConfig(
        loginName = customerJson.loginName,
        password = customerJson.password,
        favoriteCategoryId = None
      ),
      version = customerJson.version
    )

  protected def convertToJson(entity: Customer): CustomerJson = CustomerJson(
    id = Some(entity.id.value.toString),
    name = entity.name,
    sexType = entity.sexType.id,
    zipCode1 = entity.profile.postalAddress.zipCode.areaCode,
    zipCode2 = entity.profile.postalAddress.zipCode.cityCode,
    prefCode = entity.profile.postalAddress.pref.id,
    cityName = entity.profile.postalAddress.cityName,
    addressName = entity.profile.postalAddress.addressName,
    buildingName = entity.profile.postalAddress.buildingName,
    email = entity.profile.contact.email,
    phone = entity.profile.contact.phone,
    loginName = entity.config.loginName,
    password = entity.config.password,
    favoriteCategoryId = entity.config.favoriteCategoryId.map(_.value.toString),
    version = entity.version
  )

}
