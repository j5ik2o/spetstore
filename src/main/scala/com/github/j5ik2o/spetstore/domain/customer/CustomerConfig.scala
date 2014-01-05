package com.github.j5ik2o.spetstore.domain.customer

import com.github.j5ik2o.spetstore.domain.pet.{Category, CategoryRepository, CategoryId}
import com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext
import scala.util.Try

/**
 * [[com.github.j5ik2o.spetstore.domain.customer.Customer]]の設定を表す値オブジェクト。
 *
 * @param loginName ログイン名
 * @param password パスワード
 * @param favoriteCategoryId お気に入りカテゴリID
 */
case class CustomerConfig
(loginName: String,
 password: String,
 favoriteCategoryId: Option[CategoryId] = None) {

  /**
   * お気に入りのカテゴリを取得する。
   *
   * @param cr [[com.github.j5ik2o.spetstore.domain.pet.CategoryRepository]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.pet.Category]]
   */
  def favoriteCategory(implicit cr: CategoryRepository, ctx: EntityIOContext): Try[Category] =
    Try(favoriteCategoryId.get).flatMap(cr.resolve)

}


