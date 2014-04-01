package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.item.{Supplier, SupplierId}
import com.github.j5ik2o.spetstore.infrastructure.db.SupplierRecord
import scala.util.Try

private[item]
class SupplierRepositoryOnJDBC
  extends RepositoryOnJDBC[SupplierId, Supplier] with SupplierRepository {

  override type T = SupplierRecord

  override protected val mapper = SupplierRecord

  override def deleteByIdentifier(identifier: SupplierId)(implicit ctx: Ctx): Try[(This, Supplier)] = ???

  override def storeEntity(entity: Supplier)(implicit ctx: Ctx): Try[(This, Supplier)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Supplier]] = ???

  override def resolveEntity(identifier: SupplierId)(implicit ctx: Ctx): Try[Supplier] = ???
}
