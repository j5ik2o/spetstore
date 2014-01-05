package com.github.j5ik2o.spetstore.infrastructure.support

case class EntityNotFoundException[ID <: Identifier[_]](identifier: ID)
  extends Exception(s"Entity is not found(identifier = $identifier)")

