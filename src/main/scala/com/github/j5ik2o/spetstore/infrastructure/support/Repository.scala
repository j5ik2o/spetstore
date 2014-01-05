package com.github.j5ik2o.spetstore.infrastructure.support

import scala.util.Try

/**
 * DDDのリポジトリ責務を表すトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  /**
   * リポジトリの派生型。
   */
  type This <: Repository[ID, E]

  /**
   * エンティティが存在するかを検証する。
   *
   * @param identifier [[com.github.j5ik2o.spetstore.infrastructure.support.Identifier]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたBoolean。存在する場合はtrue
   * @throws EntityNotFoundException エンティティが見つからない場合
   * @throws RepositoryIOException I/Oエラー
   */
  def contains(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean]

  /**
   * 識別子からエンティティを解決する。
   *
   * @param identifier [[com.github.j5ik2o.spetstore.infrastructure.support.Identifier]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたエンティティ
   * @throws EntityNotFoundException エンティティが見つからない場合
   * @throws RepositoryIOException I/Oエラー
   */
  def resolve(identifier: ID)(implicit ctx: EntityIOContext): Try[E]

  /**
   * エンティティを保存する。
   *
   * リポジトリにエンティティを保存すると、リポジトリはそのエンティティを含む新しいインスタンスと
   * 保存されたエンティティを返します。
   *
   * @param entity エンティティ
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたタプル。
   * @throws RepositoryIOException I/Oエラー
   */
  def store(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)]

  /**
   * エンティティを削除する。
   *
   * リポジトリにエンティティを削除すると、リポジトリはそのエンティティを含まない新しいインスタンスと
   * 削除されたエンティティを返します。
   *
   * @param identifier 識別子
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたタプル。
   * @throws RepositoryIOException I/Oエラー
   */
  def delete(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)]

}
