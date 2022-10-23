# USE order_batch_db;

DROP TABLE IF EXISTS `sales`;

CREATE TABLE `sales` (
    `sales_id` BIGINT auto_increment NOT NULL PRIMARY KEY ,
    `order_at` DATE NOT NULL,
    `daily_sales` BIGINT NOT NULL DEFAULT 0
);

DROP TABLE IF EXISTS `quantity`;

CREATE TABLE `quantity` (
    `quantity_id` BIGINT auto_increment NOT NULL PRIMARY KEY ,
    `sales_id` BIGINT NOT NULL,
    `name` VARCHAR(255)	NOT NULL,
    `qty` INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE `quantity` ADD CONSTRAINT `FK_sales_TO_quantity_1` FOREIGN KEY (
    `sales_id`
)
REFERENCES `sales` (
    `sales_id`
);

