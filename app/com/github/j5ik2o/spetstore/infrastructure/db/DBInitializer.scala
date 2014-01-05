package com.github.j5ik2o.spetstore.infrastructure.db

import scalikejdbc._, SQLInterpolation._

object DBInitializer {
  def run() {
    DB readOnly {
      implicit s =>
        try {
          sql"select 1 from customer limit 1".map(_.long(1)).single().apply()
        } catch {
          case e: java.sql.SQLException =>
            DB autoCommit {
              implicit s =>
                sql"""
create table customer (
  id varchar(16) not null primary key,
  name varchar(255) not null
);
   """.execute.apply()
            }
        }
    }
  }
}
