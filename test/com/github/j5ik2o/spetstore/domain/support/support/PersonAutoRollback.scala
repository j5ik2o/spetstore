package com.github.j5ik2o.spetstore.domain.support.support

import scalikejdbc.specs2.mutable.AutoRollback
import scalikejdbc.DB

trait PersonAutoRollback extends AutoRollback {
  //override def db = DB
}
