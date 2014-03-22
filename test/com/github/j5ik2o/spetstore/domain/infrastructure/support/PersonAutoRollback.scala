package com.github.j5ik2o.spetstore.domain.infrastructure.support

import scalikejdbc.specs2.mutable.AutoRollback
import scalikejdbc.{DB, NamedDB}

trait PersonAutoRollback extends AutoRollback {
  //override def db = DB
}
