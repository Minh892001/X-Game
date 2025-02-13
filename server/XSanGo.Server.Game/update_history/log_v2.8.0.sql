#2016-01-19 日志增加帐号项
ALTER TABLE `operation_log`     ADD COLUMN `account` VARCHAR(64) NULL AFTER `vip_level`;
ALTER TABLE 今天对应的分表     ADD COLUMN `account` VARCHAR(64) NULL AFTER `vip_level`;
ALTER TABLE 明天对应的分表     ADD COLUMN `account` VARCHAR(64) NULL AFTER `vip_level`;