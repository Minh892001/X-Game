-- 新增更新语句请附加到文件末尾
#20151031 世界BOSS加死亡时间
ALTER TABLE `world_boss` ADD COLUMN `boss_death_date` datetime default null;


-- 导出  表 xsg_game1.role_recall_task 结构

#20151107 铁匠铺兑换
CREATE TABLE `role_smithy` (
  `role_id` VARCHAR(255) NOT NULL,
  `exchange_item_str` VARCHAR(5000) DEFAULT NULL,
  `exchange_refresh_date` DATETIME DEFAULT NULL,
  `exchange_refresh_num` INT(11) DEFAULT '0',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

#20151107 超级充值
CREATE TABLE `role_super_charge` (
  `role_id` VARCHAR(255) NOT NULL,
  `script_Id` VARCHAR(255) DEFAULT NULL,
  `charge_amount` INT(11) DEFAULT '0',
  `raffle_num` INT(11) DEFAULT '0',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

#20151109 超级转盘
CREATE TABLE `role_super_turntable` (
  `id` VARCHAR(255) NOT NULL,
  `last_receive_time` DATETIME NOT NULL,
  `script_Id` INT(11) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
#20151110 超级转盘角色名称
ALTER TABLE role_super_turntable ADD COLUMN `role_name` VARCHAR(32) NOT NULL;
#20151110 超级转盘公告标记
ALTER TABLE role_super_turntable ADD COLUMN `announce_flag` INT(2) DEFAULT 0;
#20151110 超级转盘vip等级
ALTER TABLE role_super_turntable ADD COLUMN `vip_level` INT(8) NOT NULL;

#20151026 合服逻辑，增加角色服务器ID项
ALTER TABLE `role`     ADD COLUMN `server_id` INT NOT NULL AFTER `double_item_out_time`;
ALTER TABLE `role` ADD INDEX `index_server_id` (`server_id`);

#20151109这里必须人工指定服务器ID
update role set server_id = 改成服务器ID;

#20151117百步穿杨刷bug物品回收
DELETE FROM role_item WHERE role_id = 'MF-100001-681196' AND (template_id = 'ydan' OR template_id = 'xdan');
#20151121北伐初始化数据bug
ALTER TABLE `role_attack_castle`     CHANGE `last_reset_node_date` `last_reset_node_date` DATETIME NULL ;
#20151124百步穿杨增加字段
alter table shoot_score_rank add column `rec` varchar(100) DEFAULT NULL;


#12月1号分割线----------
#随机PK逃跑修改
ALTER TABLE role ADD COLUMN `warmup_escape_count` INT(11) NOT NULL;
ALTER TABLE role ADD COLUMN `warmup_escape_time` DATETIME NULL DEFAULT NULL;

#老友召回
CREATE TABLE `role_friends_invitation` (
	`role_id` VARCHAR(64) NOT NULL,
	`recall_code` VARCHAR(11) NOT NULL,
	`consume_amount` INT(11) NOT NULL DEFAULT '0',
	`max_level` INT(11) NOT NULL DEFAULT '0',
	`create_time` DATETIME NOT NULL,
	PRIMARY KEY (`role_id`),
	CONSTRAINT `FK_rfi_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `role_friends_recalled` (
	`role_id` VARCHAR(64) NOT NULL,
	`invite_role_id` VARCHAR(64) NOT NULL DEFAULT '""',
	`sign_count` INT(11) NOT NULL DEFAULT '0',
	`charge_amount` INT(11) NOT NULL DEFAULT '0',
	`state` TINYINT(4) NOT NULL COMMENT '0：已符合离线天数；1: 已打开过界面；2：已被召回',
	`recall_time` DATETIME NOT NULL,
	PRIMARY KEY (`role_id`),
	CONSTRAINT `FK_rfr_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#老友召回表结构修改
ALTER TABLE `role_friends_recalled`
	CHANGE COLUMN `invite_role_id` `invite_role_id` VARCHAR(64) NULL DEFAULT '""' AFTER `role_id`;

CREATE TABLE IF NOT EXISTS `role_recall_task` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `task_id` int(11) NOT NULL,
  `state` tinyint(4) NOT NULL COMMENT '0: 未完成; 1:已完成,待领奖; 2:已领奖',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#成就表
CREATE TABLE `role_achieve` (
  `id` VARCHAR(64) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  `progress` VARCHAR(600) DEFAULT NULL,
  `continue_day_info` VARCHAR(600) DEFAULT NULL,
  `received` VARCHAR(600) DEFAULT NULL,
  `rec_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#任务活跃点
CREATE TABLE `role_task_apoint` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `point` int(11) default '0',
  `received` varchar(350) default NULL,
  `rec_time` datetime default NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#七日进度信息
CREATE TABLE `role_seven_progress` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `s_id` int(8) NOT NULL,
  `progress` varchar(100) default NULL,
  `rec_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#七日领奖信息
CREATE TABLE `role_seven_task` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `three_star_rec` varchar(50) default NULL,
  `star_award_rec` varchar(100) default NULL,
  `total_star` int(8) default '0',
  `join_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20151027 重置随从
ALTER TABLE `role_hero` ADD COLUMN `special_attendant` varchar(255) default NULL;

#20151113 副本Buff
ALTER TABLE `role` ADD COLUMN `copy_buff` INT DEFAULT '0' NOT NULL AFTER `warmup_escape_time`;

#20151114 增加完成成就数
ALTER TABLE `role`     ADD COLUMN `achieve_completed_num` INT(6) NULL DEFAULT 0 AFTER `server_id`;

## 20151120活动api
CREATE TABLE `role_api` (
	`id` VARCHAR(64) NOT NULL,
	`role_id` VARCHAR(64) NOT NULL,
	`act_id` INT(11) NOT NULL,
	`acc_count` INT(11) NOT NULL DEFAULT '0',
	`rewards_history` VARCHAR(200) NULL DEFAULT NULL COMMENT '已领奖等级,eg: 2,4,6',
	`update_time` DATETIME NOT NULL,
	`create_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_ra_role_id` (`role_id`),
	CONSTRAINT `FK_ra_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20151119 部分红点一天只发一次
CREATE TABLE `role_opened_menu` (
  `id` varchar(255) NOT NULL,
  `open_hero_admire_date` datetime DEFAULT NULL,
  `open_hero_call_date` datetime DEFAULT NULL,
  `open_make_vip_date` datetime DEFAULT NULL,
  `open_announce_date` datetime DEFAULT NULL,
  `open_vip_gift_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20151121 关卡占领优化
ALTER TABLE `role_copy` ADD COLUMN `levy_date` datetime DEFAULT NULL;
ALTER TABLE `role_copy` ADD COLUMN `occupy_date` datetime DEFAULT NULL;

#20151124成就相关字段的扩大
ALTER TABLE role_achieve modify COLUMN progress varchar(1000);
ALTER TABLE role_achieve modify COLUMN received varchar(1000);
ALTER TABLE role_achieve modify COLUMN continue_day_info varchar(600);

#20151125 修改role表account字段长度
ALTER TABLE `role` MODIFY COLUMN `account` VARCHAR(255) NOT NULL;

#20151125 铁匠铺蓝装兑换
CREATE TABLE `role_blue_smithy` (
  `role_id` varchar(255) NOT NULL,
  `exchange_item_str` varchar(5000) DEFAULT NULL,
  `exchange_refresh_date` datetime DEFAULT NULL,
  `exchange_refresh_num` int(11) DEFAULT '0',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

#2015-11-26全服邮件支持领取附加条件
ALTER TABLE `mail`     ADD COLUMN `condition` VARCHAR(255) NULL AFTER `title`;
ALTER TABLE `mail`     CHANGE `condition` `params` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL ;
 
#2015-11-28重置角色表改名次数为1
update role set rename_count = 1 where rename_count > 1;

#2015-12-2清除原有内嵌于任务系统的成就多余数据
delete FROM role_task where task_id in(3001,3002,3003,3004,3005,3006,3007,3008,3009,3010,3011,3012,3100,3101,
3102,3103,3104,3105,3106,3107,3108,3109,3110,3111,
3112,3201,3202,3203,3204,3205,3206,3207,3301,3302,3401,3402,3403,3404,3405,3406
);



#20151222---------------------

#20151124百步穿杨增加字段
alter table shoot_score_rank add column `rec` varchar(100) DEFAULT NULL;

#20151207百步穿杨邮件奖励状态 
alter table shoot_score_rank add column `send_mail` INT(2) DEFAULT 0 AFTER `rec`;

#20151207 角色表增加被邀请次数
ALTER TABLE `role` ADD COLUMN `invited_num` INT DEFAULT '0' NULL AFTER `invite_num`;

#20151216 API活动表结构调整 仅执行一次
DROP TABLE IF EXISTS `role_api`;
CREATE TABLE `role_api` (
  `id` varchar(64) NOT NULL default '',
  `role_id` varchar(64) default NULL,
  `active_id` int(11) default NULL,
  `process` varchar(200) default NULL,
  `rewards_history` varchar(200) default NULL,
  `time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
