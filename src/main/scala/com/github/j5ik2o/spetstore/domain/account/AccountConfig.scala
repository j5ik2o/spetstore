package com.github.j5ik2o.spetstore.domain.account

import com.github.j5ik2o.spetstore.domain.pet.{Category, CategoryRepository, CategoryId}
import scala.util.Try
import com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext

/**
 * [[com.github.j5ik2o.spetstore.domain.account.Account]]の設定を表す値オブジェクト。
 *
 * @param password パスワード
 * @param favoriteCategoryId お気に入りカテゴリID
 */
case class AccountConfig
(password: String,
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


