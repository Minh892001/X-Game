-- 新增更新语句请附加到文件末尾
#20150606增加用户非法状态表
CREATE TABLE `role_validation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(64) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `refer_id` varchar(64) DEFAULT NULL,
  `tag` varchar(256) DEFAULT NULL,
  `reason` text,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#20150622 Stat库增加非法战报信息表
CREATE TABLE `role_validate_info` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `account` varchar(64) DEFAULT NULL,
   `role_id` varchar(64) NOT NULL,
   `server_id` int(11) NOT NULL,
   `type` int(11) NOT NULL,
   `tag` text,
   `update_date` datetime NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#20150623公会成员表增加商品物品和荣誉字段
alter table `faction` add column shop_json text;
alter table `faction` add column shop_refresh_date datetime;
alter table `faction_member` add column honor int(11) DEFAULT 0;
alter table `faction_member` add column buy_shop_ids varchar(255) DEFAULT '';

#20150625增加武将修炼表
CREATE TABLE `hero_practice` (
  `id` varchar(64) NOT NULL,
  `hero_id` varchar(64) NOT NULL,
  `script_id` int(11) NOT NULL,
  `prop_name` varchar(50) NOT NULL,
  `color` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `exp` int(11) NOT NULL,
  `add_value` int(11) NOT NULL,
  `next_up_exp` int(11) NOT NULL,
  `indexof` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20150701公会动态表主键改成VARCHAR类型
ALTER TABLE faction_history MODIFY `id` varchar(64);


#20150704公会战
alter table `faction` add column honor int(11) DEFAULT 0;
alter table `faction_member` add column apply_date datetime DEFAULT null;
alter table `faction_member` add column death_date datetime DEFAULT null;
alter table `faction_member` add column gvg_end_date datetime DEFAULT null;

CREATE TABLE `faction_member_rank` (
  `id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL,
  `create_date` datetime NOT NULL,
  `honor` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150704 增加武将突破等级
ALTER TABLE `role_hero`     ADD COLUMN `break_level` TINYINT(4) NOT NULL AFTER `color`;

#20150706增加第一佳活动表
CREATE TABLE `role_firstjia` (
   `role_id` VARCHAR(64) NOT NULL,
   `accepted_levelrewards` VARCHAR(512) DEFAULT NULL,
   `last_update_time` DATETIME NOT NULL,
   PRIMARY KEY (`role_id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 #20150706增加每日活动活动表
CREATE TABLE `role_daylogin` (
   `role_id` VARCHAR(64) NOT NULL,
   `accepted_levelrewards` VARCHAR(512) DEFAULT NULL,
   `day_count` int DEFAULT 0,
   `last_login_time` DATETIME NOT NULL,
   `last_update_time` DATETIME NOT NULL,
   PRIMARY KEY (`role_id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
#20150707增加送军令活动表
CREATE TABLE `role_send_junling`(
	`role_id` VARCHAR(64) NOT NULL,
	`accepted_rewardindex` TEXT,
	`last_accepted_time` DATETIME,
	`last_update_time` DATETIME,
	PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#20150709充值消费活动复制
CREATE TABLE `role_big_day_charge` (
  `id` varchar(64) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `threshold` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_big_day_consume` (
  `id` varchar(64) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `threshold` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_big_activity_sum_charge` (
  `id` varchar(64) NOT NULL,
  `last_receive_time` datetime NOT NULL,
  `threshold` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_big_activity_sum_consume` (
  `id` varchar(64) NOT NULL,
  `last_receive_time` datetime NOT NULL,
  `threshold` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table `role` add column big_day_consume_yuanbao int(11) DEFAULT 0;
alter table `role` add column big_consume_yuanbao int(11) DEFAULT 0;

#20150714增加幸运大转盘系统表
CREATE TABLE `role_fortune_wheel`(
	`role_id` VARCHAR(64) NOT NULL,
	`total_count` INT NOT NULL DEFAULT '0',
	`last_count` INT NOT NULL DEFAULT '0',
	`charge_count` INT NOT NULL DEFAULT '0',
	`update_time` DATETIME NOT NULL,
	PRIMARY KEY (`role_id`)
);


#20150717增加限时武将表
CREATE TABLE `limit_hero` (
  `id` varchar(255) NOT NULL,
  `today_refresh_date` datetime DEFAULT NULL,
  `today_script_ids` varchar(255) DEFAULT NULL,
  `week_refresh_date` datetime DEFAULT NULL,
  `week_script_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20150718增加限时武将伪随机字段
alter table `role` add column buy_limit_hero_count int(11) DEFAULT 0;
alter table `role` add column buy_limit_hero_yuanbao int(11) DEFAULT 0;

#20150722role表增加first_in_time字段
ALTER TABLE `role` ADD COLUMN `first_in_time` BIGINT DEFAULT '0' NULL AFTER `buy_limit_hero_yuanbao`;

#20150724 副本热身赛相关数据
ALTER TABLE `role`     ADD COLUMN `total_warmup_count` INT NOT NULL AFTER `buy_limit_hero_yuanbao`,     
ADD COLUMN `win_warmup_count` INT NOT NULL AFTER `total_warmup_count`,     
ADD COLUMN `warmup_update_time` DATETIME NULL AFTER `win_warmup_count`;

#20150725 增加热身赛失败次数
ALTER TABLE `role`     ADD COLUMN `lose_warmup_count` INT NOT NULL AFTER `win_warmup_count`;

#20150725累计登录增加活动开始时间字段
ALTER TABLE `role_daylogin` ADD COLUMN `start_time` varchar(512) NOT NULL AFTER `last_update_time`;
#20150725累计登录表上次登录时间允许为空(活动没开始的时候)
ALTER TABLE `role_daylogin` CHANGE `last_login_time` `last_login_time` DATETIME NULL, CHANGE `last_update_time` `last_update_time` DATETIME NULL ;

#20150801公会成员表增加副本伤害字段
ALTER TABLE `faction_member` ADD COLUMN `sum_copy_harm` int DEFAULT '0';


#20150810还原最高公会等级经验
update faction set exp = 50000 where level >= 10 and exp > 50000;

# 20150810删除竞技场战报数据
delete from fight_movie;