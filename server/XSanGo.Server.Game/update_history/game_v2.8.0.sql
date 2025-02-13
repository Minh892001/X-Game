-- 新增更新语句请附加到文件末尾
#20151120 正进行觉醒的武将编号
ALTER TABLE `role` ADD COLUMN `awaken_hero_id` varchar(64) default NULL;

#20151120 武将表增加是否觉醒
ALTER TABLE `role_hero` ADD COLUMN `is_awaken` tinyint(1) default '0' COMMENT '0 no 1 yes';

#20151120 武将觉醒表
CREATE TABLE `role_hero_awaken` (
  `hero_id` varchar(64) NOT NULL,
  `plan` varchar(255) default NULL,
  `baptize_prop` varchar(512) default NULL,
  PRIMARY KEY  (`hero_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20151120 洗练师表
CREATE TABLE `role_polish_division` (
  `role_id` varchar(64) NOT NULL,
  `level` int(11) default NULL,
  `exp` int(11) default NULL,
  `free_time` datetime default NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2015-12-2清除原有内嵌于任务系统的成就多余数据
delete FROM role_task where task_id in(3001,3002,3003,3004,3005,3006,3007,3008,3009,3010,3011,3012,3100,3101,
3102,3103,3104,3105,3106,3107,3108,3109,3110,3111,
3112,3201,3202,3203,3204,3205,3206,3207,3301,3302,3401,3402,3403,3404,3405,3406);

#20151215 寻宝活动
ALTER TABLE role_treasure ADD COLUMN `activity_hero_num` INT(11) NOT NULL DEFAULT 0;

#修复arena_award_log award_str 字段过短问题
ALTER TABLE `arena_award_log` CHANGE `award_str` `award_str` MEDIUMTEXT NULL;
#20151211 公会优化
ALTER TABLE `faction` ADD COLUMN `join_level` INT NOT NULL DEFAULT 15;
ALTER TABLE `faction` ADD COLUMN `send_mail_times` INT NOT NULL DEFAULT 0;
ALTER TABLE `faction` ADD COLUMN `send_mail_date` datetime DEFAULT NULL;

#2015-12-14增加豪情宝红包索要记录表
CREATE TABLE `role_claim_redpacket` (
   `id` varchar(64) NOT NULL,
   `role_id` varchar(64) NOT NULL,
   `redpacket_id` varchar(64) NOT NULL,
   `update_date` datetime NOT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
#2015-12-15增加微信分享记录
CREATE TABLE `role_share_weixin` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `share_times` int(4) default '0',
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2015-12-15增加开服活动
CREATE TABLE `role_open_server_active` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `active_node_id` int(4) NOT NULL,
  `progress` varchar(200) default NULL,
  `update_time` datetime default NULL,
  `rec_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#2015-12-15成就进度领取
CREATE TABLE `role_achieve_progress_rec` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `received` varchar(1000) default NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#2015-12-15全服首次公告
CREATE TABLE `achieve_first_notify` (
  `id` varchar(64) NOT NULL,
  `achieve_id` int(8) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 
#20151218 发奖记录字段加长
ALTER TABLE `arena_award_log` modify COLUMN `award_str` mediumtext;

#20151219 公会继续优化
ALTER TABLE `faction` ADD COLUMN `mail_logs` text DEFAULT NULL;
ALTER TABLE `faction` ADD COLUMN `join_vip` INT NOT NULL DEFAULT 0;
ALTER TABLE `faction` ADD COLUMN `manifesto` varchar(255) DEFAULT NULL;

#20151219 公会宝库
ALTER TABLE `faction` ADD COLUMN `warehouse_level` int(11) DEFAULT 1;
ALTER TABLE `faction` ADD COLUMN `storehouse_level` int(11) DEFAULT 1;
ALTER TABLE `faction` ADD COLUMN `score` int(11) DEFAULT 0;
ALTER TABLE `faction` ADD COLUMN `ovi_store_data` text DEFAULT NULL;
ALTER TABLE `faction` ADD COLUMN `warehouse_data` text DEFAULT NULL;
#名将召唤字段更改，增加每日次数字段
ALTER TABLE `role_collect_soul` ADD COLUMN `day_gold_collect_count` INT DEFAULT '0' NOT NULL AFTER `collect_type`;
ALTER TABLE `role_collect_soul` ADD COLUMN `last_day_refresh_time` DATETIME NULL AFTER `last_refresh_time`;
##20160104增加大富温表
CREATE TABLE `lottery_record` (
  `role_id` varchar(255) NOT NULL,
  `grid_id` int(4) default '0',
  `throw_num` int(4) default '0',
  `auto_num` int(4) default '0',
  `cycle_time` int(4) default '0',
  `shop_info` varchar(350) default NULL,
  `grids_info` varchar(350) default NULL,
  `score` int(4) default '0',
  `special_score` int(4) default '0',
  `update_time` datetime default NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-1-6 删除启用的碎片掠夺相关表
DROP  TABLE  IF EXISTS role_item_chip;
DROP  TABLE  IF EXISTS role_item_chip_fight;
#20160107大富温表修改
ALTER TABLE `lottery_record` ADD COLUMN `daily_throw_num` int(4) DEFAULT 0 AFTER `grid_id`;
#20160113大富温表修改
ALTER TABLE `lottery_record` ADD COLUMN `shop_open_time` datetime DEFAULT NULL AFTER `update_time`;
#20160113大富温表修改
ALTER TABLE `lottery_record` ADD COLUMN `send_mail` int(4) DEFAULT 0 AFTER `shop_open_time`;
#20160114分享活动表
CREATE TABLE `role_share` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `task_id` int(11) NOT NULL,
  `progress` varchar(1000) default NULL,
  `share_time` datetime default NULL,
  `rec_time` datetime default NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20160109比武大会
CREATE TABLE `role_tournament` (
   `role_id` varchar(64) NOT NULL,
   `num` int(11) NOT NULL DEFAULT '0',
   `has_signup` int(11) NOT NULL DEFAULT '0',
   `start_date` datetime DEFAULT NULL,
   `buy_refresh_count` int(11) NOT NULL DEFAULT '0',
   `refresh_count` int(11) NOT NULL DEFAULT '0',
   `buy_fight_count` int(11) NOT NULL DEFAULT '0',
   `fight_count` int(11) NOT NULL DEFAULT '0',
   `formation` text NOT NULL,
   `max_rank` int(11) NOT NULL DEFAULT '0',
   `last_reset_date` datetime DEFAULT NULL,
   `update_date` datetime NOT NULL,
   PRIMARY KEY (`role_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `role_tournament_bet` (
   `id` varchar(64) NOT NULL,
   `role_id` varchar(64) NOT NULL,
   `num` int(11) NOT NULL DEFAULT '0',
   `rmby` int(11) NOT NULL DEFAULT '0',
   `fight_id` int(11) NOT NULL DEFAULT '0',
   `bet_role_id` varchar(64) NOT NULL,
   `result` int(11) NOT NULL DEFAULT '0',
   `stage` int(11) NOT NULL DEFAULT '0',
   `creat_date` datetime NOT NULL,
   `win_or_not` int(11) NOT NULL DEFAULT '0',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `role_tournament_record` (
   `id` varchar(64) NOT NULL,
   `role_id` varchar(64) NOT NULL,
   `local` int(11) NOT NULL DEFAULT '0',
   `view_data` text NOT NULL,
   `win` int(11) NOT NULL DEFAULT '0',
   `add_score` int(11) NOT NULL DEFAULT '0',
   `score` int(11) NOT NULL DEFAULT '0',
   `power` int(11) NOT NULL DEFAULT '0',
   `movie_id` varchar(64) NOT NULL,
   `create_date` datetime NOT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 ALTER TABLE `role_tournament` ADD COLUMN `last_champion_num` INT DEFAULT '0' NOT NULL AFTER `max_rank`;

#20160104 寻宝改动
ALTER TABLE `role_treasure` ADD COLUMN `speed_num` int(11) DEFAULT 0;
ALTER TABLE `role_treasure` ADD COLUMN `event_ids` varchar(255) DEFAULT NULL;
ALTER TABLE `role_treasure` ADD COLUMN `accident_ids` varchar(255) DEFAULT NULL;

CREATE TABLE `role_treasure_param` (
  `id` varchar(255) NOT NULL,
  `accident_logs` text,
  `check_accident_date` datetime DEFAULT NULL,
  `gain_num` int(11) NOT NULL,
  `refresh_date` datetime DEFAULT NULL,
  `rescue_logs` text,
  `rescue_num` int(11) NOT NULL,
  `accident_num` int(11) NOT NULL,
  `accident_red_point` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `role_treasure_param` ADD COLUMN `rescue_msg` varchar(255) DEFAULT '我的寻宝小队遇到矿难啦，快来帮帮我！';