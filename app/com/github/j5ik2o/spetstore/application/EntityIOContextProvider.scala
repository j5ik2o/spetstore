package com.github.j5ik2o.spetstore.application

import com.github.j5ik2o.spetstore.infrastructure.support.{EntityIOContextOnJDBC, EntityIOContextOnMemory, EntityIOContext}
import scalikejdbc.DBSession

trait EntityIOContextProvider {
  def get: EntityIOContext
}

object EntityIOContextProvider {

  object ofMemory extends EntityIOContextProvider {
    def get = EntityIOContextOnMemory
  }

  class ofJDBC(dbSession: DBSession) extends EntityIOContextProvider {
    def get = EntityIOContextOnJDBC(dbSession)
  }

}

