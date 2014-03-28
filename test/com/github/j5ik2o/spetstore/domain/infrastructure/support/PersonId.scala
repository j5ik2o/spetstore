package com.github.j5ik2o.spetstore.domain.infrastructure.support

import com.github.j5ik2o.spetstore.domain.lifecycle.IdentifierService

/**
 * テスト用の識別子。
 */
case class PersonId(value: Long = IdentifierService.generate(classOf[Person]))
  extends Identifier[Long]
