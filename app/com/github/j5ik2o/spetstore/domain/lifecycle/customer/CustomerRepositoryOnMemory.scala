package com.github.j5ik2o.spetstore.domain.lifecycle.customer

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, RepositoryOnMemory }
import com.github.j5ik2o.spetstore.domain.model.customer.{ Customer, CustomerId }
import scala.util.Try

/**
 * [[CustomerRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[customer] class CustomerRepositoryOnMemory(entities: Map[CustomerId, Customer])
    extends RepositoryOnMemory[CustomerId, Customer](entities) with CustomerRepository {

  protected def createInstance(entities: Map[CustomerId, Customer]): This =
    new CustomerRepositoryOnMemory(entities)

  def resolveByLoginName(loginName: String)(implicit ctx: EntityIOContext): Try[Option[Customer]] = Try {
    entities.map(_._2).toList.find(_.config.loginName == loginName)
  }

}
