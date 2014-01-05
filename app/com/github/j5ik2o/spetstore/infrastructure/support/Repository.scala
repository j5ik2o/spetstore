package com.github.j5ik2o.spetstore.infrastructure.support

import scala.util.{Failure, Success, Try}

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

  protected final def forEachEntities[A](tasks: Seq[A])(processor: (This, A) => Try[(This, E)])
                                        (implicit ctx: EntityIOContext): Try[(This, Seq[E])] = Try {
    val result = tasks.foldLeft[(This, Seq[E])]((this.asInstanceOf[This], Seq.empty[E])) {
      (resultWithEntities, task) =>
        val resultWithEntity = processor(resultWithEntities._1, task).get
        (resultWithEntity._1.asInstanceOf[This], resultWithEntities._2 :+ resultWithEntity._2)
    }
    (result._1, result._2)
  }


  /**
   * エンティティが存在するかを検証する。
   *
   * @param identifier [[com.github.j5ik2o.spetstore.infrastructure.support.Identifier]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたBoolean。存在する場合はtrue
   * @throws EntityNotFoundException エンティティが見つからない場合
   * @throws RepositoryIOException I/Oエラー
   */
  def containsByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean]

  def containsByIdentifiers(identifiers: Seq[ID])(implicit ctx: EntityIOContext): Try[Boolean] =
    traverse(identifiers, force = false)(containsByIdentifier).map(_.forall(_ == true))

  /**
   * 識別子からエンティティを解決する。
   *
   * @param identifier [[com.github.j5ik2o.spetstore.infrastructure.support.Identifier]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたエンティティ
   * @throws EntityNotFoundException エンティティが見つからない場合
   * @throws RepositoryIOException I/Oエラー
   */
  def resolveEntity(identifier: ID)(implicit ctx: EntityIOContext): Try[E]

  protected def traverse[V, R](values: Seq[V], force: Boolean = true)(f: (V) => Try[R])
                              (implicit ctx: EntityIOContext): Try[Seq[R]] = {
    values.map(f).foldLeft(Try(Seq.empty[R])) {
      (resultTry, resolveTry) =>
        resultTry.flatMap {
          result =>
            resolveTry.map {
              entity =>
                result :+ entity
            }.recoverWith {
              case ex: EntityNotFoundException =>
                if (force) {
                  Success(result)
                } else {
                  Failure(ex)
                }
            }
        }
    }
  }

  /**
   * 複数の識別子に対応するエンティティを解決する。
   *
   * `resolveEntity`を使って複数のエンティティを解決します。リポジトリが対応するストレージによっては
   * 効率がよくない場合がある。その場合は、このメソッドをオーバーライドすべきです。
   *
   * @param identities 識別子の集合
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたエンティティ
   * @throws RepositoryIOException I/Oエラー
   */
  def resolveEntities(identities: Seq[ID])(implicit ctx: EntityIOContext): Try[Seq[E]] =
    traverse(identities)(resolveEntity)

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
  def storeEntity(entity: E)(implicit ctx: EntityIOContext): Try[(This, E)]

  def storeEntities(entities: Seq[E])(implicit ctx: EntityIOContext): Try[(This, Seq[E])] =
    forEachEntities(entities) {
      (repository, entity) =>
        repository.storeEntity(entity).asInstanceOf[Try[(This, E)]]
    }

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
  def deleteByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[(This, E)]

  def deleteByIdentifiers(identities: ID*)(implicit ctx: EntityIOContext): Try[(This, Seq[E])] =
    forEachEntities(identities) {
      (repository, identity) =>
        repository.deleteByIdentifier(identity).asInstanceOf[Try[(This, E)]]
    }

}
