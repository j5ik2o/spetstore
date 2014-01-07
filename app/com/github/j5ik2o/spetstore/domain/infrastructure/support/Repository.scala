package com.github.j5ik2o.spetstore.domain.infrastructure.support

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

  /**
   * リポジトリの状態の更新が伴う複数の処理をシーケンシャルに実行する。
   *
   * 複数の更新操作を行う場合はこのメソッドを必ず利用すること。戻り値は必ず(This, Seq[E])となる。
   * 最初の更新操作で新しく作られたリポジトリインスタンスを使って、次の更新操作を行う。
   * 途中で例外が発生した場合は処理の継続を断念する。
   * 
   * @param values 入力となる値の集合
   * @param processor 値を処理するための関数
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @tparam V 値の方
   * @return `Try`でラップされた`(This, Seq[E])`
   */
  protected final def sequential[V](values: Seq[V])(processor: (This, V) => Try[(This, E)])
                                        (implicit ctx: EntityIOContext): Try[(This, Seq[E])] = Try {
    val result = values.foldLeft[(This, Seq[E])]((this.asInstanceOf[This], Seq.empty[E])) {
      (resultWithEntities, task) =>
        val resultWithEntity = processor(resultWithEntities._1, task).get
        (resultWithEntity._1.asInstanceOf[This], resultWithEntities._2 :+ resultWithEntity._2)
    }
    (result._1, result._2)
  }
  
  /**
   * 引数に渡した値の集合を基に`Try[R]`を返す関数を実行し、最後に`Try[Seq[R]`に変換して返すユーティリティメソッド。
   *
   * 更新操作を行わない場合はこのメソッドを使う。戻り値は自由に指定できる。
   * 途中で例外が発生した場合は、処理の継続を断念しますが、EntityNotFoundExceptionだけ無視することができる。
   * デフォルトは無視する。
   *
   * @param values 入力となる値の集合
   * @param force EntityNotFoundExceptionを無視するフラグ
   * @param processor 値を処理するための関数
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @tparam V 値の方
   * @tparam R 戻り値の方
   * @return `Try`でラップされた`Seq[R]`
   */
  protected final def traverse[V, R](values: Seq[V], force: Boolean = true)(processor: (V) => Try[R])
                              (implicit ctx: EntityIOContext): Try[Seq[R]] = {
    values.map(processor).foldLeft(Try(Seq.empty[R])) {
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
   * エンティティが存在するかを検証する。
   *
   * @param identifier [[com.github.j5ik2o.spetstore.infrastructure.support.Identifier]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたBoolean。存在する場合はtrue
   * @throws EntityNotFoundException エンティティが見つからない場合
   * @throws RepositoryIOException I/Oエラー
   */
  def containsByIdentifier(identifier: ID)(implicit ctx: EntityIOContext): Try[Boolean]

  /**
   * 複数の識別子を指定して、エンティティが存在するかを検証する。
   *
   * `containsByIdentifier`を使って複数のエンティティの存在を検証します。
   * リポジトリが対応するストレージにとって、効率がよくない場合は、このメソッドをオーバーライドすべきです。
   *
   * @param identifiers 識別子の集合
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたエンティティ
   * @throws RepositoryIOException I/Oエラー
   */
  def containsByIdentifiers(identifiers: Seq[ID])(implicit ctx: EntityIOContext): Try[Boolean] =
    traverse(identifiers, force = false)(containsByIdentifier).map(_.forall(_ == true))

  /**
   * 識別子からエンティティを解決する。
   *
   * @param identifier [[com.github.j5ik2o.spetstore.infrastructure.support.Identifier]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   *
   * @return `Try`でラップされたエンティティ
   * @throws EntityNotFoundException エンティティが見つからない場合
   * @throws RepositoryIOException I/Oエラー
   */
  def resolveEntity(identifier: ID)(implicit ctx: EntityIOContext): Try[E]


  /**
   * 複数の識別子に対応するエンティティを解決する。
   *
   * `resolveEntity`を使って複数のエンティティを解決します。
   * リポジトリが対応するストレージにとって、効率がよくない場合は、このメソッドをオーバーライドすべきです。
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

  /**
   * 複数のエンティティを保存する。
   *
   * `storeEntity`を使って複数のエンティティを保存します。
   * リポジトリが対応するストレージにとって、効率がよくない場合は、このメソッドをオーバーライドすべきです。
   *
   * @param entities エンティティの集合
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたエンティティ
   * @throws RepositoryIOException I/Oエラー
   */
  def storeEntities(entities: Seq[E])(implicit ctx: EntityIOContext): Try[(This, Seq[E])] =
    sequential(entities) {
      (repository, entity) =>
        repository.storeEntity(entity).asInstanceOf[Try[(This, E)]]
    }

  /**
   * 識別子を指定して、エンティティを削除する。
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

  /**
   * 複数の識別子を指定して、エンティティを削除する。
   *
   * `deleteByIdentifier`を使って複数のエンティティを削除します。
   * リポジトリが対応するストレージにとって、効率がよくない場合は、このメソッドをオーバーライドすべきです。
   *
   * @param identifiers 識別子の集合
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`でラップされたエンティティ
   * @throws RepositoryIOException I/Oエラー
   */
  def deleteByIdentifiers(identifiers: ID*)(implicit ctx: EntityIOContext): Try[(This, Seq[E])] =
    sequential(identifiers) {
      (repository, identity) =>
        repository.deleteByIdentifier(identity).asInstanceOf[Try[(This, E)]]
    }

}
