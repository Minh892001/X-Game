#20151026合服附加功能，服务器入口保留，增加目标服务器字段
ALTER TABLE `game_server` ADD COLUMN `target_id` INT DEFAULT '0' NOT NULL AFTER `online_limit`;
UPDATE game_server SET target_id = id;