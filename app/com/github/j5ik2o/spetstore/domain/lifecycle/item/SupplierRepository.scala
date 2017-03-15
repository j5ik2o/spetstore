package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.Repository
import com.github.j5ik2o.spetstore.domain.model.item.{ Supplier, SupplierId }

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Supplier]]のためのリポジトリ責務。
 */
trait SupplierRepository extends Repository[SupplierId, Supplier] {

  type This = SupplierRepository

}

/**
 * コンパニオンオブジェクト。
 */
object SupplierRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository]]
   */
  def ofMemory(entities: Map[SupplierId, Supplier] = Map.empty): SupplierRepository =
    new SupplierRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository]]
   */
  def ofJDBC: SupplierRepository = new SupplierRepositoryOnJDBC

}
