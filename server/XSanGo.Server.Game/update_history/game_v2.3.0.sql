-- 新增更新语句请附加到文件末尾
#20151009 字段超限
ALTER TABLE `role_item`     CHANGE `attach_data` `attach_data` VARCHAR(1024) ;

#20151010双倍卡
ALTER TABLE `role` ADD COLUMN `double_exp_out_time` datetime;
ALTER TABLE `role` ADD COLUMN `double_item_out_time` datetime;


#20151010增加好友互送体力功能
ALTER TABLE `role_sns` 
	ADD COLUMN `send_junling_num` INT DEFAULT '0' NULL AFTER `role_id`,
	ADD COLUMN `accept_junling_num` INT DEFAULT '0' NULL AFTER `send_junling_num`,
	ADD COLUMN `accept_junling_time` DATETIME NULL AFTER `accept_junling_num`,
	ADD COLUMN `friend_point` INT DEFAULT '0' NULL AFTER `target`;
CREATE TABLE `role_sns_junling_limit` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `role_id` varchar(64) NOT NULL,
   `target_id` varchar(64) NOT NULL,
   `send_num` int(11) DEFAULT '0',
   `send_time` datetime DEFAULT NULL,
   `recv_num` int(11) DEFAULT '0',
   `recv_time` datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20151013聚贤庄加物品购买次数
ALTER TABLE `role_limit_hero` ADD COLUMN `item_buy_count` int(11) default 0;


ALTER TABLE `role_attack_castle`  ADD PRIMARY KEY(`role_id`);


# 95服玩家 赵笑怡 嘲讽模式状态手动清除(只在android95服执行)
UPDATE role_arena_rank SET sneer_id = 0, sneer_str = '', guard_win_sum = 0 WHERE role_id = 'MF-100095-596676';



#20151017 世界boss
CREATE TABLE `world_boss` (
  `id` varchar(255) NOT NULL,
  `boss_id` int(11) DEFAULT NULL,
  `boss_remain_blood` bigint(20) DEFAULT NULL,
  `boss_sum_blood` bigint(20) DEFAULT NULL,
  `count_rank_json` text,
  `customs_id` int(11) DEFAULT NULL,
  `harm_rank_json` text,
  `participator_ids` text,
  `tail_award_json` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_world_boss` (
  `id` varchar(255) NOT NULL,
  `challenge_date` datetime DEFAULT NULL,
  `inspire_type` int(11) DEFAULT NULL,
  `inspire_value` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20151020公会产出控制
CREATE TABLE `role_faction` (
  `id` varchar(255) NOT NULL,
  `buy_shop_ids` varchar(255) DEFAULT NULL,
  `copy_award_num` int(11) NOT NULL,
  `shop_refresh_date` datetime DEFAULT NULL,
  `copy_challenge_num` int(11) NOT NULL,
  `limit_time_award_num` int(11) NOT NULL,
  `receive_award_date` datetime DEFAULT NULL,
  `refresh_challenge_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `role_item`     CHANGE `attach_data` `attach_data` VARCHAR(1024) ;

#20151020豪情宝
CREATE TABLE `haoqingbao_item_packet` (
   `id` varchar(64) NOT NULL,
   `sender_id` varchar(64) NOT NULL,
   `receiver_id` varchar(64) DEFAULT NULL,
   `item_id` varchar(64) NOT NULL,
   `num` int(11) NOT NULL DEFAULT '0',
   `lucky_star` int(11) NOT NULL DEFAULT '0',
   `send_date` datetime NOT NULL,
   `receive_date` datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 CREATE TABLE `role_haoqingbao` (
   `role_id` varchar(64) NOT NULL,
   `yuanbao_num` int(11) NOT NULL DEFAULT '0',
   `recv_num` int(11) NOT NULL DEFAULT '0',
   `total_count` bigint(20) NOT NULL DEFAULT '0',
   `total_num` bigint(20) NOT NULL DEFAULT '0',
   `send_num` int(11) NOT NULL DEFAULT '0',
   `last_recv_time` datetime DEFAULT NULL,
   `update_time` datetime NOT NULL,
   `lucky_star_count` int(11) NOT NULL DEFAULT '0',
   `total_send_count` bigint(20) NOT NULL DEFAULT '0',
   `total_send_sum` bigint(20) NOT NULL DEFAULT '0',
   `last_charge_status_time` bigint(20) NOT NULL DEFAULT '0',
   PRIMARY KEY (`role_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 CREATE TABLE `role_haoqingbao_item` (
   `id` varchar(64) NOT NULL,
   `role_id` varchar(64) NOT NULL,
   `send_range` int(11) NOT NULL DEFAULT '0',
   `type` int(11) NOT NULL DEFAULT '0',
   `min_level` int(11) NOT NULL DEFAULT '0',
   `min_vip_level` int(11) NOT NULL DEFAULT '0',
   `min_friend_point` int(11) NOT NULL DEFAULT '0',
   `total_num` int(11) NOT NULL DEFAULT '0',
   `redpacket_num` int(11) NOT NULL DEFAULT '0',
   `received_num` int(11) NOT NULL DEFAULT '0',
   `msg` text,
   `faction_id` varchar(64) DEFAULT NULL,
   `start_time` datetime NOT NULL,
   `last_recv_time` datetime DEFAULT NULL,
   `finished` int(11) NOT NULL DEFAULT '0',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 CREATE TABLE `role_haoqingbao_record` (
   `id` varchar(64) NOT NULL,
   `role_id` varchar(64) NOT NULL,
   `yuanbao_num` int(11) NOT NULL DEFAULT '0',
   `description` text NOT NULL,
   `update_time` datetime NOT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 CREATE TABLE `role_haoqingbao_redpacket_record` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `role_id` varchar(64) NOT NULL,
   `redpacket_id` varchar(64) NOT NULL,
   `sender_id` varchar(64) NOT NULL,
   `num` int(11) NOT NULL DEFAULT '0',
   `lucky_star` int(11) NOT NULL DEFAULT '0',
   `recv_date` datetime NOT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
#20151024 百步穿杨积分表
CREATE TABLE `shoot_score_rank` (
  `role_id` varchar(255) NOT NULL,
  `rank` int(4) default '0',
  `score` int(4) default '0',
  `score_recv_done` int(4) default '0',
  `total_score` int(11) default '0',
  `day_one_cnt` int(4) default '0',
  `shoot_one_cnt` int(4) default '0',
  `shoot_ten_cnt` int(4) default '0',
  `shoot_ont_time` datetime,
  `shoot_time` datetime NOT NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20151026 世界BOSS活动
ALTER TABLE `role_world_boss` ADD COLUMN `gain_item_num` int(11) default 0;
ALTER TABLE `role_world_boss` ADD COLUMN `gain_item_date` datetime default null;


#20151022 累计登录永久
 CREATE TABLE `role_dayforverlogin` (
  `role_id` varchar(64) NOT NULL,
  `accepted_levelrewards` varchar(512) default NULL,
  `day_count` int(11) default '0',
  `last_login_time` datetime default NULL,
  `last_update_time` datetime default NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20151027 百步穿杨积分表增加字段
truncate table shoot_score_rank;
ALTER TABLE `shoot_score_rank` ADD COLUMN `day_free_cnt` int(4) default 0 after `total_score`;
ALTER TABLE `shoot_score_rank` ADD COLUMN `shoot_free_time` datetime after `shoot_ten_cnt`;


#20151030 修炼表加索引
ALTER TABLE `hero_practice` ADD INDEX hero_practice_index_hero_id (`hero_id`);

#20151030 群雄争霸加积分字段
ALTER TABLE `role_ladder` ADD COLUMN `ladder_score` int default 0;