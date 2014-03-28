package com.github.j5ik2o.spetstore.domain.lifecycle

import java.util.concurrent.atomic.AtomicInteger

object IdentifierService {

  val ids = collection.mutable.Map.empty[Class[_], AtomicInteger]

  def generate(entity: Class[_]): Long = {
    val ai = ids.getOrElseUpdate(entity, new AtomicInteger())
    ai.getAndIncrement
  }

}
