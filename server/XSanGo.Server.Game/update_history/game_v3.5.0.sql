-- 新增更新语句请附加到文件末尾
#20160405 世界BOSS托管次数限制
ALTER TABLE `role_world_boss` ADD COLUMN `trust_num` int(11) default 0;
ALTER TABLE `role_world_boss` ADD COLUMN `trust_refresh_date` datetime default NULL;

#20160412 群雄争霸加机器人
ALTER TABLE `role` ADD COLUMN `robot_type` int(11) default 0;


#20160401 线上版本集中优化
ALTER TABLE auction_house_record ADD INDEX(auction_id);
ALTER TABLE faction_battle_member ADD INDEX(faction_id);
ALTER TABLE role_achieve ADD INDEX(role_id);
ALTER TABLE role_achieve_progress_rec ADD INDEX(role_id);
ALTER TABLE role_api ADD INDEX(role_id);
ALTER TABLE role_big_activity_sum_charge ADD INDEX(role_id);
ALTER TABLE role_big_activity_sum_consume ADD INDEX(role_id);
ALTER TABLE role_big_day_charge ADD INDEX(role_id);
ALTER TABLE role_big_day_consume ADD INDEX(role_id);
ALTER TABLE role_claim_redpacket ADD INDEX(role_id);
ALTER TABLE role_cornucopia ADD INDEX(role_id);
ALTER TABLE role_daily_login ADD INDEX(role_id);
ALTER TABLE role_haoqingbao_record ADD INDEX(role_id);
ALTER TABLE role_haoqingbao_redpacket_record ADD INDEX(role_id);
ALTER TABLE role_invite_activity ADD INDEX(role_id);
ALTER TABLE role_lucky_bag ADD INDEX(role_id);
ALTER TABLE role_open_server_active ADD INDEX(role_id);
ALTER TABLE role_recall_task ADD INDEX(role_id);
ALTER TABLE role_resource_accept ADD INDEX(role_id);
ALTER TABLE role_seven_progress ADD INDEX(role_id);
ALTER TABLE role_seven_task ADD INDEX(role_id);
ALTER TABLE role_share ADD INDEX(role_id);
ALTER TABLE role_share_weixin ADD INDEX(role_id);
ALTER TABLE role_sns_junling_limit ADD INDEX(role_id);
ALTER TABLE role_super_turntable ADD INDEX(role_id),ADD INDEX(announce_flag);
ALTER TABLE role_task_apoint ADD INDEX(role_id);
ALTER TABLE role_tournament_record ADD INDEX(role_id);
ALTER TABLE role_treasure ADD INDEX(role_id);
ALTER TABLE role_validation ADD INDEX(role_id);
ALTER TABLE worship_rank ADD INDEX(role_id);


#20160426 修复重复触发进阶红包问题
ALTER TABLE `role_task_apoint` ADD COLUMN `max_quality_level` INT(11) default 0;

#20160426修复公会战成员死亡次数的问题
ALTER TABLE `faction_battle_member` CHANGE `deaths` `deaths` int(11)   NULL DEFAULT '0' after `even_kill_num`;

#20160414 公会优化
ALTER TABLE `role_faction` ADD COLUMN `donate_weizhang` int(11) default 0;
ALTER TABLE `role_faction` ADD COLUMN `donate_yuanbao` int(11) default 0;
ALTER TABLE `faction` ADD COLUMN `recommend_num` int(11) default 0;
ALTER TABLE `faction` ADD COLUMN `recommend_refresh_date` datetime default NULL;
ALTER TABLE `faction_member` ADD COLUMN `donate_logs` text;
UPDATE `faction_member` SET `donate_logs` = '[]';
ALTER TABLE `faction` ADD COLUMN `purchase_logs` text;
UPDATE `faction` SET `purchase_logs` = '[]';
ALTER TABLE `faction_member` ADD COLUMN `demand_item` varchar(100) default NULL;

#20160419武将觉醒二次开发
ALTER TABLE `role` DROP COLUMN `awaken_hero_id`;
ALTER TABLE `role_hero` DROP COLUMN `is_awaken`;
DROP TABLE `role_polish_division`; 
DROP TABLE `role_hero_awaken`;
CREATE TABLE `role_hero_awaken` (
  `hero_id` varchar(64) NOT NULL,
  `awaken_star` int(11) default '0',
  `baptize_lvl` int(11) default '0',
  `baptize_prop` varchar(512) default NULL,
  PRIMARY KEY  (`hero_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20160426 跨服竞技场
CREATE TABLE `role_cross_arena` (
  `role_id` varchar(64) NOT NULL,
  `challenge` int(11) NOT NULL,
  `challenge_buy` int(11) NOT NULL,
  `refresh_date` datetime DEFAULT NULL,
  `cross_arena_logs` text,
  `challenge_date` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20160427南华幻境
CREATE TABLE `role_dreamland` (
  `role_id` varchar(64) NOT NULL default '',
  `cur_scene_id` int(11) default '0',
  `scene_plan` varchar(1024) default NULL,
  `nan_hua` int(11) default '0',
  `shop_refresh_num` int(11) default '0',
  `shop_items` varchar(255) default NULL,
  `star_award` varchar(512) default NULL,
  `star_num` int(11) default '0',
  `layer_num` int(11) default '0',
  `last_refresh_time` datetime default NULL,
  `last_shop_refresh_time` datetime default NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20160414 公会官员邀请
ALTER TABLE `faction` ADD COLUMN `delete_day` int(11) default 0;
ALTER TABLE `faction` ADD COLUMN `recruit_num` int(11) default 0;
ALTER TABLE `faction` ADD COLUMN `invite_num` int(11) default 0;
ALTER TABLE `faction` ADD COLUMN `refresh_recruit_date` datetime default NULL;

#201600509 公会官员邀请2
ALTER TABLE `faction` DROP COLUMN `invite_num`;
ALTER TABLE `role_faction` ADD COLUMN `invite_num` int(11) default 0;

#20160512新版公会战第三版
CREATE TABLE `faction_battle_config` (
  `id` varchar(64) NOT NULL default '',
  `first_faction_id` varchar(64) default NULL,
  `first_camp_id` int(11) default '0',
  `enroll_camp_id` varchar(50) default NULL,
  `robot_create_time` datetime default NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `faction_battle_robot` (
  `id` varchar(64) NOT NULL default '',
  `stronghold_id` int(11) default '0',
  `robot_role_id` varchar(64) default NULL,
  `debuff_lvl` int(11) default '0',
  `be_attack_wait_endtime` datetime default NULL,
  `time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-04-11 煮酒论英雄参数记录
CREATE TABLE `role_makewine` (
  `role_id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `vip` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '0',
  `head_img` varchar(50) DEFAULT '0',
  `compose_score` int(11) NOT NULL DEFAULT '0' COMMENT 'niangjiu jifen',
  `share_score` int(11) NOT NULL DEFAULT '0' COMMENT 'fenxiang jifen',
  `exchange_used_socre` int(11) NOT NULL DEFAULT '0' COMMENT 'used jifen',
  `receive_material_date` datetime DEFAULT NULL,
  `receive_socre` int(11) NOT NULL DEFAULT '0',
  `item_composed_count` varchar(50) DEFAULT NULL COMMENT '[50,50,50,50]',
  `receive_share_date` datetime DEFAULT NULL,
  `toped_times` tinyint(3) NOT NULL DEFAULT '0',
  `reset_date` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-04-11 煮酒论英雄 分享记录
CREATE TABLE `role_makewine_share_record` (
  `id` varchar(50) NOT NULL,
  `role_id` varchar(50) NOT NULL,
  `config_id` int(11) NOT NULL DEFAULT '0',
  `role_name` varchar(50) NOT NULL,
  `last_count` int(11) NOT NULL DEFAULT '0',
  `top` tinyint(3) NOT NULL DEFAULT '0',
  `top_time` datetime DEFAULT NULL,
  `received_player` varchar(500) DEFAULT NULL,
  `share_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-04-12 好友切磋战报记录
CREATE TABLE `role_challenge_summary` (
  `role_id` varchar(50) NOT NULL,
  `success_times` int(11) NOT NULL DEFAULT '0',
  `fail_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`role_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-04-21 百步穿杨新增是否显示中奖记录字段
ALTER TABLE `shoot_score_rank` ADD COLUMN `show_my_record` TINYINT(3) DEFAULT '1' NOT NULL AFTER `send_mail`;
ALTER TABLE `shoot_score_rank` ADD COLUMN `histroy_award` TEXT NULL AFTER `show_my_record`; 
ALTER TABLE `shoot_score_rank` DROP COLUMN `score`; 
ALTER TABLE `shoot_score_rank` DROP COLUMN `score_recv_done`; 
ALTER TABLE `shoot_score_rank` CHANGE `shoot_time` `shoot_time` DATETIME NULL ;
ALTER TABLE `shoot_score_rank` ADD COLUMN `shoot_cnt_super` INT(11) DEFAULT '0' NOT NULL AFTER `histroy_award`;

#2016-05-12 觉醒二次开发二轮迭代
ALTER TABLE `role_hero_awaken` ADD COLUMN `baptize_consume` varchar(255)  COLLATE utf8_general_ci NULL after `baptize_prop`;

#2016-05-16 觉醒二次开发三轮迭代
ALTER TABLE `role_hero_awaken` ADD COLUMN `is_awaken` TINYINT(1) DEFAULT '0' NOT NULL AFTER `awaken_star`;

#2016-05-18 南华二次开发
ALTER TABLE `role_dreamland` ADD COLUMN `today_scene_plan` varchar(512)  COLLATE utf8_general_ci NULL after `scene_plan`;
ALTER TABLE `role_dreamland` ADD COLUMN `challenge_num` int(11)   NULL DEFAULT '0' after `today_scene_plan`;
ALTER TABLE `role_dreamland` ADD COLUMN `buy_num` int(11)   NULL DEFAULT '0' after `challenge_num`;
ALTER TABLE `role_dreamland` CHANGE `cur_scene_id` `last_challenge_scene_id` int(11)   NULL DEFAULT '0' after `role_id`;

#2016-05-19成就修改进度类型
ALTER TABLE `role_achieve` CHANGE `progress` `progress` TEXT NULL ; 
UPDATE role_achieve SET progress = LEFT('hm1001,hm1002,hm1003,hm2001,hm2002,hm2003,hm2004,hm2005,hm2006,hm2007,hm2008,hm2009,hm2010,hm2011,hm2012,hm2013,hm2014,hm2015,hm3001,hm3002,hm3003,hm3004,hm3005,hm3006,hm3007,hm3008,hm3009,hm3010,hm3011,hm3012,hm3013,hm3014,hm3015,hm3016,hm3017,hm3018,hm3019,hm3020,hm4001,hm4002,hm4003,hm4004,hm4005,hm4006,hm4007,hm4008,hm4009,hm4010,hm4011,hm4012,hm4013,hm4014,hm4015,hm4016,hm4017,hm4018,hm4019,hm4020,wp1001,wp1002,wp1003,wp1004,wp2001,wp2002,wp2003,wp2004,wp2005,wp2006,wp2007,wp2008,wp2009,wp2010,wp2011,wp2012,wp2013,wp2014,wp2015,wp2016,wp2017,wp2018,wp3001,wp3002,wp3003,wp3004,wp3005,wp3006,wp3007,wp3008,wp3009,wp3010,wp3011,wp3012,wp3013,wp3014,wp3015,wp3016,wp3017,wp3018,wp3019,wp3020,wp3021,wp3022,wp4001,wp4002,wp4003,wp4004,wp4005,wp4006,wp4007,wp4008,wp4009,wp4010,wp4011,wp4012,wp4013,wp4014,wp4015,wp4016,wp4017,wp4018,wp4019,wp4020,wp4021,wp4022,am1001,am1002,am1003,am2001,am2002,am2003,am2004,am2005,am2006,am2007,am2008,am2009,am2010,am2011,am2012,am2013,am2014,am2015,am3001,am3002,am3003,am3004,am3005,am3006,am3007,am3008,am3009,am3010,am3011,am3012,am3013,am3014,am3015,am3016,am3017,am3018,am3019,am4001,am4002,am4003,am4004,am4005,am4006,am4007,am4008,am4009,am4010,am4011,am4012,am4013,am4014,am4015,am4016,am4017,am4018,am4019,bt1001,bt1002,bt2001,bt2002,bt2003,bt2004,bt2005,bt2006,bt2007,bt2010,bt2008,bt2009,bt2011,bt2012,bt3001,bt3002,bt3003,bt3004,bt3005,bt3006,bt3007,bt3008,bt3009,bt3010,bt3011,bt3012,bt3013,bt3014,bt3015,bt3016,bt3017,bt4001,bt4002,bt4003,bt4004,bt4005,bt4006,bt4007,bt4008,bt4009,bt4010,bt4011,bt4012,bt4013,bt4014,bt4015,bt4016,bt4017,or1001,or1002,or1003,or2001,or2002,or2003,or2004,or2005,or2006,or2007,or2008,or2009,or2010,or2011,or2012,or2013,or2014,or3001,or3002,or3003,or3004,or3005,or3006,or3007,or3008,or3009,or3010,or3011,or3012,or3013,or3014,or3015,or3016,or3017,or4001,or4002,or4003,or4004,or4005,or4006,or4007,or4008,or4009,or4010,or4011,or4012,or4013,or4014,or4015,or4016,or4017,tr1001,tr1002,tr2001,tr2002,tr2003,tr2004,tr2005,tr2006,tr2007,tr2008,tr2009,tr2010,tr3001,tr3002,tr3003,tr3004,tr3005,tr3006,tr3007,tr3008,tr3009,tr3010,tr3011,tr3012,tr3013,tr4001,tr4002,tr4003,tr4004,tr4005,tr4006,tr4007,tr4008,tr4009,tr4010,tr4011,tr4012,tr4013,rd1001,rd1002,rd2001,rd2002,rd2003,rd2004,rd2005,rd2006,rd2007,rd2008,rd2009,rd3001,rd3002,rd3003,rd3004,rd3005,rd3006,rd3007,rd3008,rd3009,rd3010,rd3011,rd3012,rd4001,rd4002,rd4003,rd4004,rd4005,rd4006,rd4007,rd4008,rd4009,rd4010,rd4011,rd4012,', progress * 7) WHERE TYPE = 'EquipTypes';

#2016-05-23 南华二次开发二轮迭代
ALTER TABLE `role_dreamland` DROP COLUMN `today_scene_plan`;

#2016-05-26 加升级时间
ALTER TABLE `role` ADD COLUMN `level_up_date` datetime DEFAULT NULL;

#2016-06-02 欧洲杯活动
CREATE TABLE `football_bet` (
  `id` varchar(64) NOT NULL,
  `group_id` int(11) DEFAULT '0',
  `role_id` varchar(255) DEFAULT NULL,
  `bet_country_id` int(11) DEFAULT '0',
  `bet_num` int(11) DEFAULT '0',
  `is_award` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `role_football` (
  `role_id` varchar(64) NOT NULL,
  `buy_num` int(11) DEFAULT '0',
  `is_open` int(11) DEFAULT '0',
  `is_over_open` int(11) DEFAULT '0',
  `refresh_date` datetime DEFAULT NULL,
  `bet_logs` text,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-06-06 线上觉醒技能数据清理
delete from role_hero_skill where template_id = 42019527;
delete from role_hero_skill where template_id = 41019527;

#2016-06-07跨服竞技场日志记录字段改编码
ALTER TABLE `role_cross_arena` CHANGE `cross_arena_logs` `cross_arena_logs` text CHARACTER SET utf8mb4 NOT NULL;

#2016-06-23公会副本发奖优化
ALTER TABLE `role_faction` ADD COLUMN `award_faction_id` VARCHAR(255) DEFAULT NULL;

#2016-07-06公会宣言改编码
ALTER TABLE `faction` CHANGE `manifesto` `manifesto` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL;

#寻宝求援私信改编码
ALTER TABLE `role_treasure_param` CHANGE `rescue_msg` `rescue_msg` varchar(255) CHARACTER SET utf8mb4;