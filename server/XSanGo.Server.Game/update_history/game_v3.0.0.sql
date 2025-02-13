-- 新增更新语句请附加到文件末尾
#20160125比武大会优化
ALTER TABLE `role_tournament` ADD COLUMN `win_num` int(11) default 0;

  #20160123彩蛋活动
CREATE TABLE `role_colorfullegg` (
  `role_id` VARCHAR(255) NOT NULL,
  `broken_time` DATETIME DEFAULT NULL,
  `receive_time` DATETIME DEFAULT NULL,
  `start_time` DATETIME DEFAULT NULL,
  `record` VARCHAR(255) DEFAULT NULL,
  `accept_script_Id` VARCHAR(255) DEFAULT NULL,
  `accept_number` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

# 20160201 资源找回活动
CREATE TABLE `role_resource_accept` (
   `id` VARCHAR(64) NOT NULL,
   `role_id` VARCHAR(64) NOT NULL,
   `type` INT(11) NOT NULL DEFAULT '0',
   `tag` VARCHAR(256) NOT NULL,
   `name` VARCHAR(256) NOT NULL,
   `is_received` INT(11) NOT NULL DEFAULT '0',
   `recv_count` INT(11) NOT NULL DEFAULT '0',
   `total_count` INT(11) NOT NULL DEFAULT '0',
   `date_tag` VARCHAR(64) NOT NULL,
   `update_time` DATETIME NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `role_daily_login` (
   `id` VARCHAR(64) NOT NULL,
   `role_id` VARCHAR(64) NOT NULL,
   `date_tag` VARCHAR(64) NOT NULL,
   `update_time` DATETIME NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#2016-03-03 邮件表emoji表情支持
ALTER TABLE mail CHANGE `body` `body` VARCHAR(512) CHARACTER SET utf8mb4 NOT NULL,CHANGE `title` `title` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL;
ALTER TABLE role_mail CHANGE `body` `body` VARCHAR(1000) CHARACTER SET utf8mb4 NOT NULL,CHANGE `title` `title` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL;

# 20160226 阵法进阶
ALTER TABLE `role_formation` ADD COLUMN `advanced_type` int(11) default 0;
ALTER TABLE `role_formation` ADD COLUMN `advanceds` varchar(500) default '[]';

#20160305 世界BOSS托管
UPDATE world_boss SET participator_ids = '';
ALTER TABLE `role_world_boss` ADD COLUMN `trust_yuanbao` int(11) default 0;
ALTER TABLE `role_world_boss` ADD COLUMN `trust_items` varchar(500) default '[]';
# 20160303 比武大会优化
ALTER TABLE `role_tournament` ADD COLUMN `shop_buy_count` TEXT NULL AFTER `win_num`;
ALTER TABLE `role_tournament` ADD COLUMN `coin` INT DEFAULT '0' NOT NULL AFTER `shop_buy_count`;
ALTER TABLE `role_tournament` ADD COLUMN `ybcoin` INT DEFAULT '0' NOT NULL AFTER `coin`;

#2016-03-08 主公新增头像边框
ALTER TABLE `role` CHANGE `head_image` `head_image` VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' NOT NULL; 
ALTER TABLE `role` ADD COLUMN `ext_head_image` VARCHAR(64) DEFAULT '' NULL AFTER `head_image`; 
ALTER TABLE `role` ADD COLUMN `head_border` VARCHAR(10) DEFAULT '' NULL AFTER `ext_head_image`;
ALTER TABLE `role` ADD COLUMN `ext_head_border` VARCHAR(64) DEFAULT '' NULL AFTER `head_border`;
#2016-03-11 修改离线消息字符集
ALTER TABLE `chat_message_offline` CHANGE `sender_content` `sender_content` VARCHAR(1000) CHARACTER SET utf8mb4 NULL;
ALTER TABLE `chat_message` CHANGE `content` `content` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL;

#2015-03-05 比武大会头像存服务器(找出第一名的roleId替换`???`,在ta所在的数据库执行)
UPDATE `role` SET `ext_head_image` = (CASE sex WHEN 1 THEN 'frist01,' ELSE 'frist02,' END) WHERE `id`='???';

#20160315 公会科技
ALTER TABLE `role_faction` ADD COLUMN `can_yuanbao_donate` int(11) default 0;
ALTER TABLE `faction` ADD COLUMN `technology_data` text;
ALTER TABLE `faction` DROP COLUMN warehouse_level;
ALTER TABLE `faction` DROP COLUMN storehouse_level;
ALTER TABLE `faction` ADD COLUMN study_num int(11) default 0;

#20160310公会战相关数据表
CREATE TABLE `faction_battle` (
  `faction_id` varchar(64) NOT NULL default '',
  `enroll_time` datetime default NULL,
  `camp_stronghold_id` int(11) default NULL,
  `enroll_role_id` varchar(64) default NULL,
  `badge` int(11) default '0',
  `forage` int(11) default '0',
  `kill_num` int(11) default '0',
  `update_time` datetime default NULL,
  PRIMARY KEY  (`faction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `faction_battle_event` (
  `stronghold_id` int(11) NOT NULL default '0',
  `event_id` int(11) default NULL,
  `event_items` varchar(50) default NULL,
  `draw_state` tinyint(1) default '0',
  `event_time` datetime default NULL,
  PRIMARY KEY  (`stronghold_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `faction_battle_event_ratio` (
  `id` varchar(64) NOT NULL default '',
  `role_id` varchar(64) default NULL,
  `stronghold_id` int(11) default NULL,
  `random_num` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `faction_battle_log` (
  `id` varchar(64) NOT NULL default '',
  `role_id` varchar(64) default NULL,
  `type` tinyint(1) default '0',
  `log_key` varchar(50) default NULL,
  `log_value` varchar(512) default NULL,
  `time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `faction_battle_member` (
  `role_id` varchar(64) NOT NULL default '',
  `faction_id` varchar(64) default NULL,
  `stronghold_id` int(11) default NULL,
  `enter_stronghold_time` datetime default NULL,
  `attack_wait_endtime` datetime default NULL,
  `be_attack_wait_endtime` datetime default NULL,
  `relive_endtime` datetime default NULL,
  `marching_cooling_endtime` datetime default NULL,
  `marching_cooling_cd_num` int(11) default '0',
  `digging_treasure_endtime` datetime default NULL,
  `digging_treasure_cd_num` int(11) default '0',
  `debuff_lvl` int(11) default '0',
  `items` varchar(512) default NULL,
  `badge` int(11) default '0',
  `forage` int(11) default '0',
  `kill_num` int(11) default '0',
  `even_kill_num` int(11) default '0',
  `deaths` tinyint(3) default NULL,
  `update_time` datetime default NULL,
  `join_time` datetime default NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20160323 公会宝库改动
ALTER TABLE `role_faction` ADD COLUMN `allot_item_num` int(11) default 0;
ALTER TABLE `role_faction` ADD COLUMN `refresh_allot_date` datetime default NULL;
ALTER TABLE `role_faction` modify COLUMN can_yuanbao_donate varchar(500);
UPDATE `role_faction` SET can_yuanbao_donate = NULL;

#20160323 公会仓库加分配日志
ALTER TABLE `faction` ADD COLUMN `allot_log` text;