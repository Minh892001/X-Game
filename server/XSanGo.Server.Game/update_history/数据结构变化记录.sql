#20150602日志表增加vip_level字段
ALTER TABLE `operation_log` ADD COLUMN `vip_level` INT NOT NULL AFTER `yuanbao`;

#20150529战报FightMovie表, 增加验证状态字段
ALTER TABLE `fight_movie` ADD COLUMN `validated` INT DEFAULT '0' NOT NULL AFTER `end_time`;

#20150529全局秒杀商品加日期
alter table `seckill_item` add column create_date datetime DEFAULT '2015-05-28 00:00:00';

#20150528增加战力嘉奖活动表
create table `role_powerreward`( 
   `role_id` varchar(64) NOT NULL , 
   `last_update_time` datetime NOT NULL , 
   `accepted_powerrewards` varchar(255) , 
   PRIMARY KEY (`role_id`)
 );

#20150528更改战报 FightMovie 表, 增加类型,双方详细信息等字段
alter table `fight_movie` 
   add column `type` int DEFAULT '0' NOT NULL after `date_time`, 
   add column `self_role_id` varchar(64) NOT NULL after `type`, 
   add column `opponent_role_id` varchar(64) NOT NULL after `self_role_id`, 
   add column `result` int DEFAULT '0' NOT NULL after `opponent_role_id`, 
   add column `remain_hero` int DEFAULT '0' NOT NULL after `result`, 
   add column `self_formation_json` text NOT NULL after `remain_hero`, 
   add column `opponent_formation_json` text NOT NULL after `self_formation_json`;

#20150528战报表FightMovie增加开始和结束时间
alter table `fight_movie` 
   add column `end_time` datetime NULL after `opponent_formation_json`,
   CHANGE `date_time` `start_time` DATETIME NOT NULL;
   
#20150527基金活动
CREATE TABLE `role_fund` (
  `role_id` VARCHAR(255) NOT NULL,
  `accepted_rewards` VARCHAR(255) DEFAULT NULL,
  `buy_fund` INT(11) NOT NULL DEFAULT '0',
  `last_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
#20150527冲级有奖活动
CREATE TABLE `role_levelreward` (
  `role_id` VARCHAR(255) NOT NULL,
  `accepted_levelrewards` VARCHAR(255) DEFAULT NULL,
  `last_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#20150527秒杀活动、日充值消费活动
CREATE TABLE `seckill_item` (
  `id` varchar(255) NOT NULL,
  `buy_num` int(11) DEFAULT NULL,
  `seckill_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `role_seckill` (
  `id` varchar(64) NOT NULL,
  `seckill_date` datetime DEFAULT NULL,
  `seckill_id` int(11) DEFAULT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8C3BB866E29D9213` (`role_id`),
  CONSTRAINT `FK8C3BB866E29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_day_charge` (
  `id` varchar(64) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `threshold` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE06EA420E29D9213` (`role_id`),
  CONSTRAINT `FKE06EA420E29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `role_day_consume` (
  `id` varchar(64) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `threshold` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3A0FA810E29D9213` (`role_id`),
  CONSTRAINT `FK3A0FA810E29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `role` ADD COLUMN `day_consume_yuanbao` INT DEFAULT 0;

#20150527封测返利发放记录
CREATE TABLE `role_fourth_test` (
  `role_id` VARCHAR(255) NOT NULL,
  `charge_return` BIT(1) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#20150522清理未使用邀请码
DELETE FROM invite_code WHERE use_role_id IS NULL;

#20150521个别渠道帐号超长
ALTER TABLE `role`
CHANGE `account` `account` VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
 

#20150508拍卖行增加商店
ALTER TABLE `role_auction_house`
ADD COLUMN `refresh_shop_times` INT DEFAULT '0',
ADD COLUMN `last_refresh_date` DATETIME NULL,
ADD COLUMN `shop_item_json` text DEFAULT NULL;

#20150507购买军令相关结构
ALTER TABLE `role`     
ADD COLUMN `buy_junling_num` INT DEFAULT '0' NULL AFTER `buy_jinbi_time`,
ADD COLUMN `buy_junling_time` DATETIME NULL AFTER `buy_junling_num`,
ADD COLUMN `next_junling_time` DATETIME NULL AFTER `invite_num`;
 
#20150429 清空任务
delete from red_packet;
delete from role_red_packet;
delete from role_task;

#20150422 好友邀请记录表
CREATE TABLE `role_invite_log` (
  `id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL,
  `create_date` datetime NOT NULL,
  `target_id` varchar(255) NOT NULL,
  `mac` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150420 增加拍卖行log表
CREATE TABLE `auction_house_item_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auction_id` varchar(64) NOT NULL,
  `item_id` varchar(64) NOT NULL,
  `data` text,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#20150418 加长战报录像表的data字段
alter table `fight_movie` change `data` `data` mediumtext NOT NULL;

#20150416 原宝箱伪随机表改名，原名字留给全局伪随机控制
RENAME TABLE `role_mock` TO `role_chest_mock`;

CREATE TABLE `role_mock` (
  `id` VARCHAR(255) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  `template_id` VARCHAR(255) NOT NULL,
  `sucess_num` INT(11) NOT NULL,  
  `total_num` INT(11) NOT NULL,  
  PRIMARY KEY (`id`),
  KEY `FK14044BB3E29D9213` (`role_id`),
  CONSTRAINT `FK14044BB3E29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#20150414 邀请好友
ALTER TABLE `role` ADD COLUMN `invite_code` varchar(8);
ALTER TABLE `role` ADD COLUMN `invite_num` int(11) default 0;

CREATE TABLE `role_invite_activity` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `threshold` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `invite_code` (
  `id` varchar(64) NOT NULL,
  `code` varchar(8) NOT NULL,
  `use_role_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20150414 更改竞技场战报表,增加录像字段
ALTER TABLE `role_arena_rank_fight`  ADD COLUMN `movie_id` varchar(64) NOT NULL;
#20150414 更改战报录像表,删除老表role_fight_movie新建新表fight_movie
DROP TABLE IF EXISTS `role_fight_movie`;
CREATE TABLE `fight_movie` (
  `id` varchar(64) NOT NULL,
  `data` text NOT NULL,
  `date_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20150411 增加竞技场战报录像表
CREATE TABLE `role_fight_movie` (
  `id` varchar(64) NOT NULL,
  `fight_report_id` varchar(64) NOT NULL,
  `data` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150408增加role表chapter_tag的长度
alter table `role` change `chapter_tag` `chapter_tag` varchar(2048) DEFAULT NULL;

#20150408拍卖行表结构
CREATE TABLE `auction_house_item` (
  `id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `base_price` bigint(20) NOT NULL DEFAULT '0',
  `fixed_price` bigint(20) NOT NULL DEFAULT '-1',
  `bid_num` int(11) NOT NULL DEFAULT '0',
  `current_price` bigint(20) NOT NULL DEFAULT '0',
  `bid_role_id` varchar(64) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` bigint(20) NOT NULL DEFAULT '0',
  `item_json` text NOT NULL,
  `item_type` int(11) NOT NULL DEFAULT '0',
  `item_num` int(11) NOT NULL DEFAULT '0',
  `pause_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `auction_house_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(64) NOT NULL,
  `other_id` varchar(64) DEFAULT NULL,
  `operation_type` int(11) NOT NULL,
  `template_id` varchar(64) NOT NULL,
  `update_time` datetime NOT NULL,
  `auction_id` varchar(64) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `money` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=785 DEFAULT CHARSET=utf8;

CREATE TABLE `auction_house_record` (
  `id` varchar(64) NOT NULL,
  `auction_id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  `price` bigint(20) NOT NULL DEFAULT '0',
  `count` int(11) NOT NULL DEFAULT '0',
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_auction_house` (
  `role_id` varchar(64) NOT NULL,
  `money` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150407公会入会申请表增加申请时间
ALTER TABLE `faction_req`  ADD COLUMN `req_date` DATETIME DEFAULT '1970-01-01 00:00:00';

#20150407武将缘分表
CREATE TABLE `role_hero_relation` (
  `id` VARCHAR(64) NOT NULL,
  `hero_id` VARCHAR(64) NOT NULL,
  `level` INT(11) NOT NULL,
  `template_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `hero_index` (`hero_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#20150406删除关卡占领表
DELETE FROM server_copy;

#20150403公会副本加成
ALTER TABLE `faction_copy` ADD COLUMN `addition_type` INT(11) DEFAULT 0 NOT NULL;

#20150402邮件附件支持实例物品
TRUNCATE TABLE `mail`;
TRUNCATE TABLE `role_mail`;
ALTER TABLE `role_mail`     CHANGE `attachments` `attachments` VARCHAR(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE `mail`     CHANGE `attachments` `attachments` VARCHAR(4096) NOT NULL;

#20150331全服红包系统
CREATE TABLE `role_red_packet` (
  `id` varchar(255) NOT NULL,
  `red_packet_ids` text DEFAULT NULL,
  `is_tong_guan` bit(1) DEFAULT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1DD90B5FE29D9213` (`role_id`),
  CONSTRAINT `FK1DD90B5FE29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `red_packet` (
  `id` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `sender_id` varchar(255) NOT NULL,
  `task_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150326公会动态增加vip等级
ALTER TABLE `faction_history` ADD COLUMN `vip_level` INT(11) DEFAULT 0 NOT NULL;

-- 20150323 去除 role_collect_soul 表的 collect_count 字段的 zerofill
ALTER TABLE `role_collect_soul` CHANGE `collect_count` `collect_count` INT(11) DEFAULT '0' NOT NULL;

#20150323名将召唤表增加索引
ALTER TABLE `role_collect_soul` ADD INDEX `index_role` (`role_id`);

-- 20150311 增加 role_vip 表累计充值(charge_history)字段, 增加 role_charge 表单次充值金额(charge_money)字段
alter table `role_vip` add column `charge_history` int(20) DEFAULT '0' NOT NULL after `vip_level`;
ALTER TABLE `role_charge` ADD COLUMN `charge_money` INT(11) DEFAULT '0' NOT NULL AFTER `role_id`;

#20150309增加角色表帐号长度
ALTER TABLE `role` CHANGE `account` `account` VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE `role_mail` CHANGE `body` `body` VARCHAR(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

-- 20150304 公会加副本开启次数限制
ALTER TABLE `faction`  ADD COLUMN `last_open_copy_time` DATETIME;
ALTER TABLE `faction`  ADD COLUMN `open_copy_num` INT NOT NULL;
-- 20150302 公会副本增加掉血量
ALTER TABLE `faction_copy`  ADD COLUMN `harm_blood` INT NOT NULL;
-- 20150303 增加群雄争霸的等级或星级变化时间
alter table role_ladder add `change_level_date` datetime NOT NULL default ‘1970-01-01 00:00:00’;

-- 20150206 删除VIP答题次数字段
alter table role drop column right_answer_times;

-- 20150204 北伐数据库
CREATE TABLE `role_attack_castle` (
  `role_id` varchar(255) NOT NULL,
  `current_node_id` int(11) NOT NULL DEFAULT '0',
  `coin_count` int(11) NOT NULL DEFAULT '0',
  `reset_node_count` int(11) NOT NULL DEFAULT '0',
  `refresh_shop_count` int(11) NOT NULL DEFAULT '0',
  `last_reset_shop_date` datetime NOT NULL,
  `last_reset_node_date` datetime NOT NULL,
  `extra_json` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 20150127 最高战力快照
CREATE TABLE `battle_power_snapshot` (
  `role_id` VARCHAR(64) NOT NULL,
  `data` TEXT NOT NULL,
  `power` INT(11) NOT NULL,
  `role_level` INT(11) NOT NULL,
  `role_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

-- 20150126 群雄争霸数据库
alter table role_ladder add `season_date` datetime NOT NULL;

-- 20150123 增加累计消费充值活动数据结构
CREATE TABLE `role_activity_sum_charge` (
  `id` VARCHAR(255) NOT NULL,
  `last_receive_time` DATETIME NOT NULL,
  `threshold` INT(11) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_charge_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `role_activity_sum_consume` (
  `id` VARCHAR(255) NOT NULL,
  `last_receive_time` DATETIME NOT NULL,
  `threshold` INT(11) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_consume_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE `role`  ADD COLUMN `consume_yuanbao` INT DEFAULT '0' NOT NULL AFTER `right_answer_times`,
ADD COLUMN `last_consume_time` DATETIME NULL AFTER `consume_yuanbao`;

-- 20150120 公会副本
ALTER TABLE `faction_member`  ADD COLUMN `last_challenge_date` DATETIME;
ALTER TABLE `faction_member`  ADD COLUMN `challenge_num` INT NOT NULL;

CREATE TABLE `faction_copy` (
  `id` varchar(255) NOT NULL,
  `faction_id` varchar(255) NOT NULL,
  `copy_id` int(11) NOT NULL,
  `role_id` varchar(255) DEFAULT NULL,
  `stage_num` int(11) NOT NULL,
  `monster_json` varchar(500) NOT NULL,
  `open_date` datetime DEFAULT NULL,
  `challenge_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 20150115 增加名将仰慕 数据库
CREATE TABLE `role_hero_admire` (
  `role_id` varchar(255) NOT NULL,
  `hero_id` int(11) DEFAULT '0',
  `hero_list` varchar(255) DEFAULT NULL,
  `hero_refresh_date` datetime DEFAULT NULL,
  `item_list` varchar(255) DEFAULT NULL,
  `item_refresh_date` datetime DEFAULT NULL,
  `value` int(11) DEFAULT '0',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 20150114 增加名将召唤数据库
CREATE TABLE `role_collect_soul` (
  `id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL,
  `collect_type` int(11) NOT NULL,
  `collect_count` int(11) unsigned zerofill NOT NULL,
  `last_refresh_time` datetime NOT NULL,
  `view_json` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 20150112 修改数据库字段
alter table arena_award_log modify column award_str text;
alter table arena_award_log add award_type int(3) DEFAULT '1';

-- 20150109 增加群雄争霸数据库结构
CREATE TABLE `role_ladder` (
  `role_id` varchar(255) NOT NULL,
  `challenge_buy_date` datetime DEFAULT NULL,
  `challenge_buy_num` int(11) DEFAULT '0',
  `challenge_remain` int(11) DEFAULT '0',
  `guard_id` varchar(255) DEFAULT NULL,
  `ladder_level` int(11) DEFAULT '0',
  `ladder_star` int(11) DEFAULT '0',
  `reward_str` varchar(255) DEFAULT NULL,
  `show_report_date` datetime DEFAULT NULL,
  `win_num` int(11) DEFAULT '0',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_ladder_report` (
  `id` varchar(255) NOT NULL,
  `fight_id` varchar(255) NOT NULL,
  `fight_time` datetime NOT NULL,
  `level_change` int(11) DEFAULT '0',
  `rival_id` varchar(255) NOT NULL,
  `rival_level` int(11) DEFAULT '0',
  `star_change` int(11) DEFAULT '0',
  `state` int(11) DEFAULT '0',
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK3BA4CFE8E29D9213` (`role_id`),
  CONSTRAINT `FK3BA4CFE8E29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 20150109增加部队配置内容长度
ALTER TABLE `role_formation`
CHANGE `config` `config` VARCHAR(512) 
CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

-- 20150107我要做VIP添加答对次数和答题时间字段
ALTER TABLE `role`  ADD COLUMN `last_answer_date` DATETIME;
ALTER TABLE `role`  ADD COLUMN `right_answer_times` INT NOT NULL;
-- 20150105时空战役倒计时字段
ALTER TABLE `role_time_battle`  ADD COLUMN `last_pass_date` DATETIME;
-- 20150106签到抽奖伪随机计数器
ALTER TABLE role ADD COLUMN sign_count_for_tc INT NOT NULL DEFAULT 0

-- 20141226变更公告阅读记录存放方式
DROP TABLE `server_activity`;
CREATE TABLE `role_announce` (
  `id` VARCHAR(255) NOT NULL,
  `announce_id` INT(11) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9C816A12E29D9213` (`role_id`),
  CONSTRAINT `FK9C816A12E29D9213` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 20141217宝箱伪随机数据表
CREATE TABLE `role_mock` (
  `id` VARCHAR(255) NOT NULL,
  `num` INT(11) NOT NULL,
  `template_id` VARCHAR(255) NOT NULL,
  `role_id` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 2014-12-16 修改邮件的附件长度
alter table mail modify column attachments varchar(1000);
alter table role_mail modify column attachments varchar(1000);

-- 2014-12-12 添加竞技场发送奖励日志表
CREATE TABLE `arena_award_log` (
  `award_date` datetime NOT NULL,
  `award_str` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`award_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 改变离线消息 保存的长度
alter table chat_message_offline modify column sender_content varchar(1000);

-- 20141209竞技场数据结构变更
CREATE TABLE `arena_rank_temp` (
  `role_id` varchar(255) NOT NULL,
  `rank` int(11) DEFAULT '0',
  `robot` bit(1) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_arena_rank` (
  `role_id` varchar(255) NOT NULL,
  `attack_fight_sum` int(11) DEFAULT '1',
  `attack_win_sum` int(11) DEFAULT '1',
  `challenge` int(11) DEFAULT '0',
  `challenge_buy` int(11) DEFAULT '0',
  `challenge_buy_date` datetime DEFAULT NULL,
  `challenge_money` int(11) DEFAULT '0',
  `clear_cd_Date` datetime DEFAULT NULL,
  `clear_cd_num` int(11) DEFAULT '0',
  `exchange_item_str` varchar(5000) DEFAULT NULL,
  `exchange_refresh_date` datetime DEFAULT NULL,
  `exchange_refresh_num` int(11) DEFAULT '0',
  `fight_date` datetime DEFAULT NULL,
  `guard_fight_sum` int(11) DEFAULT '1',
  `guard_id` varchar(255) DEFAULT NULL,
  `guard_win_num` int(11) DEFAULT '0',
  `guard_win_sum` int(11) DEFAULT '1',
  `max_rank` int(11) DEFAULT '0',
  `show_report_date` datetime DEFAULT NULL,
  `sneer_id` int(11) DEFAULT '0',
  `sneer_str` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into arena_rank_temp select role_id, rank, robot from arena_rank;

insert into role_arena_rank select role_id, 
  attack_fight_sum,
  attack_win_sum,
  challenge,
  challenge_buy,
  challenge_buy_date,
  challenge_money,
  clear_cd_Date,
  clear_cd_num,
  exchange_item_str,
  exchange_refresh_date,
  exchange_refresh_num,
  fight_date datetime,
  guard_fight_sum,
  guard_id,
  guard_win_num,
  guard_win_sum,
  max_rank,
  show_report_date,
  sneer_id,
  sneer_str
  from arena_rank;

drop table arena_rank;

RENAME TABLE arena_rank_temp TO arena_rank;

-- 20141205增加引导统计表，角色表增加所属渠道，变更帐号命名规则，和一号通同步
UPDATE role SET account = REPLACE(account,'@','.');

ALTER TABLE `role` ADD COLUMN `reg_channel` INT NOT NULL AFTER `account`;

CREATE TABLE `server_guide_counter` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `counter` INT(11) NOT NULL,
  `guide_id` INT(11) NOT NULL,
  `server_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FKFACB0BBDC54D8B15` (`server_id`),
  CONSTRAINT `FKFACB0BBDC54D8B15` FOREIGN KEY (`server_id`) REFERENCES `server_config` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 20141204签到表增加是否补签标志,补签不能增加累计签到礼包需要次数
ALTER TABLE `role_sign` ADD COLUMN `resign_flag` int(1) NOT NULL DEFAULT 0 AFTER `role_id`;

-- 20141202角色表增加退出公会时间
ALTER TABLE `role`  ADD COLUMN `quit_faction_date` DATETIME;