#2015-12-07增加货币类型
ALTER TABLE `charge`     ADD COLUMN `currency` VARCHAR(64) NULL AFTER `state`;