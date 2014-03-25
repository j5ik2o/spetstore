package com.github.j5ik2o.spetstore.domain.infrastructure.db

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
CREATE TABLE `customer` (
  `id`            VARCHAR(64)   NOT NULL,
  `status`        INT           NOT NULL,
  `name`          VARCHAR(256)  NOT NULL,
  `sex_type`      INT       NOT NULL,
  `zip_code`      VARCHAR(20)   NOT NULL,
  `pref_code`     INT           NOT NULL,
  `city_name`     VARCHAR(256)  NOT NULL,
  `address_name`  VARCHAR(256)  NOT NULL,
  `building_name` VARCHAR(256),
  `email`         VARCHAR(64)   NOT NULL,
  `phone`         VARCHAR(64)   NOT NULL,
  `login_name`    VARCHAR(64)   NOT NULL,
  `password`      VARCHAR(64)   NOT NULL,
  `favorite_category_id`  VARCHAR(64),
  PRIMARY KEY(`id`)
);
   """.execute().apply()
            }
        }
    }
  }
}
