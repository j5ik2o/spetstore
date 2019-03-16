CREATE TABLE `user_account` (
  `id`              bigint       NOT NULL ,
  `status`          enum('active', 'suspended', 'deleted') NOT NULL default 'active',
  `email_address`   varchar(255) NOT NULL,
  `first_name`      varchar(255) NOT NULL,
  `last_name`       varchar(255) NOT NULL,
  `hashed_password` varchar(255) NOT NULL,
  `created_at`      datetime(6)  NOT NULL,
  `updated_at`      datetime(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_account_id_sequence_number`(id bigint unsigned NOT NULL) ENGINE=MyISAM;
INSERT INTO `user_account_id_sequence_number` VALUES (100);

CREATE TABLE `item` (
  `id`          bigint NOT NULL ,
  `status`      enum('active', 'suspended', 'deleted') NOT NULL default 'active',
  `name`        varchar(255) NOT NULL,
  `description` varchar(255),
  `categories`  varchar(255) NOT NULL,
  `price`       bigint NOT NULL,
  `created_at`  datetime(6) NOT NULL,
  `updated_at`  datetime(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `item_id_sequence_number`(id bigint unsigned NOT NULL) ENGINE=MyISAM;
INSERT INTO `item_id_sequence_number` VALUES (100);