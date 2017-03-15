package com.github.j5ik2o.spetstore.application

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContextOnJDBC, EntityIOContextOnMemory, EntityIOContext }
import scalikejdbc.DBSession

trait EntityIOContextProvider {
  def get: EntityIOContext
}

object EntityIOContextProvider {

  object Memory extends EntityIOContextProvider {
    def get = EntityIOContextOnMemory
  }

  class JDBC(dbSession: DBSession) extends EntityIOContextProvider {
    def get = EntityIOContextOnJDBC(dbSession)
  }

}

