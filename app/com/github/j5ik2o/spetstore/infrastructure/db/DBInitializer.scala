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
DROP TABLE IF EXISTS `customer`;
""".execute().apply()
sql"""
CREATE TABLE `customer` (
  `pk`                    BIGINT        NOT NULL AUTO_INCREMENT,
  `id`                    BIGINT        NOT NULL,
  `status`                INT           NOT NULL,
  `name`                  VARCHAR(256)  NOT NULL,
  `sex_type`              INT       NOT NULL,
  `zip_code`              VARCHAR(20)   NOT NULL,
  `pref_code`             INT           NOT NULL,
  `city_name`             VARCHAR(256)  NOT NULL,
  `address_name`          VARCHAR(256)  NOT NULL,
  `building_name`         VARCHAR(256),
  `email`                 VARCHAR(64)   NOT NULL,
  `phone`                 VARCHAR(64)   NOT NULL,
  `login_name`            VARCHAR(64)   NOT NULL,
  `password`              VARCHAR(64)   NOT NULL,
  `favorite_category_id`  BIGINT,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);
""".execute().apply()

              sql"""
DROP TABLE IF EXISTS `category`;
""".execute().apply()
sql"""
CREATE TABLE `category` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `id`          BIGINT        NOT NULL,
  `status`      INT           NOT NULL,
  `name`        VARCHAR(256)  NOT NULL,
  `description` VARCHAR(1024),
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);
""".execute().apply()
sql"""
CREATE TABLE `cart` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `id`          BIGINT        NOT NULL,
  `status`      INT           NOT NULL,
  `customer_id` BIGINT,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);
""".execute().apply()
                sql"""
CREATE TABLE `cart_item` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `no`          INT           NOT NULL,
  `status`      INT           NOT NULL,
  `cart_id`     BIGINT        NOT NULL,
  `item_id`     BIGINT        NOT NULL,
  `quantity`    INT           NOT NULL,
  `in_stock`    INT           NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`no`,`cart_id`)
);

""".execute().apply()
            }
        }
    }
  }
}
