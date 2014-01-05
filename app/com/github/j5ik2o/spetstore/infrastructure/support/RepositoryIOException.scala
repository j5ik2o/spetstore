package com.github.j5ik2o.spetstore.infrastructure.support

/**
 * リポジトリ内部で発生するI/O例外。
 *
 * @param message メッセージ
 */
case class RepositoryIOException(message: String) extends Exception(message)

