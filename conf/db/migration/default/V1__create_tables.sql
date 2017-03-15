-- DROP TABLE IF EXISTS `customer`;
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
  `version`               BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);

-- DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `id`          BIGINT        NOT NULL,
  `status`      INT           NOT NULL,
  `name`        VARCHAR(256)  NOT NULL,
  `description` VARCHAR(1024),
  `version`     BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);

-- DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `id`          BIGINT        NOT NULL,
  `status`      INT           NOT NULL,
  `customer_id` BIGINT        NOT NULL,
  `version`     BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);

CREATE TABLE `item` (
  `pk`            BIGINT        NOT NULL AUTO_INCREMENT,
  `id`            BIGINT        NOT NULL,
  `status`        INT           NOT NULL,
  `item_type_id`  BIGINT        NOT NULL,
  `name`          VARCHAR(256)  NOT NULL,
  `description`   VARCHAR(256),
  `price`         INT           NOT NULL,
  `supplier_id`   BIGINT        NOT NULL,
  `version`       BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);

-- DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `id`          BIGINT        NOT NULL,
  `status`      INT           NOT NULL,
  `cart_id`     BIGINT        NOT NULL,
  `no`          INT           NOT NULL,
  `item_id`     BIGINT        NOT NULL,
  `quantity`    INT           NOT NULL,
  `in_stock`    INT           NOT NULL,
  `version`     BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`),
  UNIQUE(`cart_id`,`no`)
);

-- DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `pk`            BIGINT        NOT NULL AUTO_INCREMENT,
  `id`            BIGINT        NOT NULL,
  `status`        INT           NOT NULL,
  `order_status`  INT           NOT NULL,
  `order_date`    TIMESTAMP     NOT NULL,
  `customer_id`   BIGINT        NOT NULL,
  `customer_name` VARCHAR(256)  NOT NULL,
  `zip_code`      VARCHAR(20)   NOT NULL,
  `pref_code`     INT           NOT NULL,
  `city_name`     VARCHAR(256)  NOT NULL,
  `address_name`  VARCHAR(256)  NOT NULL,
  `building_name` VARCHAR(256),
  `email`         VARCHAR(64)   NOT NULL,
  `phone`         VARCHAR(64)   NOT NULL,
  `version`       BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`)
);

-- DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `pk`          BIGINT        NOT NULL AUTO_INCREMENT,
  `id`          BIGINT        NOT NULL,
  `status`      INT           NOT NULL,
  `order_id`    BIGINT        NOT NULL,
  `no`          INT           NOT NULL,
  `item_id`     BIGINT        NOT NULL,
  `quantity`    INT           NOT NULL,
  `version`     BIGINT        NOT NULL,
  PRIMARY KEY(`pk`),
  UNIQUE(`id`),
  UNIQUE(`order_id`,`no`)
);