package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, RepositoryOnJDBC }
import com.github.j5ik2o.spetstore.domain.model.item.{ Supplier, SupplierId }
import com.github.j5ik2o.spetstore.infrastructure.db.SupplierRecord
import scala.util.Try

private[item] class SupplierRepositoryOnJDBC
    extends RepositoryOnJDBC[SupplierId, Supplier] with SupplierRepository {

  override type T = SupplierRecord

  override protected lazy val mapper = SupplierRecord

  override def deleteById(identifier: SupplierId)(implicit ctx: Ctx): Try[(This, Supplier)] = ???

  override def store(entity: Supplier)(implicit ctx: Ctx): Try[(This, Supplier)] = ???

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Supplier]] = ???

  override def resolveById(identifier: SupplierId)(implicit ctx: Ctx): Try[Supplier] = ???
}
