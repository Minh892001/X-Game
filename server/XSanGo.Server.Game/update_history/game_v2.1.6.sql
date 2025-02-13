-- 新增更新语句请附加到文件末尾
#20150812增加幸运大转盘活动开始时间
ALTER TABLE `role_fortune_wheel` ADD COLUMN `activity_start_time` VARCHAR(64) NULL AFTER `update_time`;

#20150817线上发现DB压力过大，已经在线上版本执行过
ALTER TABLE fight_movie ADD INDEX end_time(end_time);

#20150817充值福袋
CREATE TABLE `role_lucky_bag` (
  `id` varchar(255) NOT NULL,
  `last_receive_time` datetime NOT NULL,
  `script_Id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150825聊天禁言记录
CREATE TABLE `role_chat_vote_forbidden` (
  `id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL,
  `target_id` varchar(255) NOT NULL,
  `vote_ids` varchar(1024) DEFAULT NULL,
  `add_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_chat_vote` (`role_id`),
  KEY `FK_chat_vote_target` (`target_id`),
  CONSTRAINT `FK_chat_vote` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_chat_vote_target` FOREIGN KEY (`target_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150820 聚宝盆
ALTER TABLE `role` ADD COLUMN `is_receive_cornucopia` int(1) NOT NULL default 0;
CREATE TABLE `role_cornucopia` (
  `id` varchar(64) NOT NULL,
  `buy_date` datetime DEFAULT NULL,
  `last_receive_date` datetime DEFAULT NULL,
  `script_id` int(11) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#20150820限时武将无VIP限制活动
ALTER TABLE `role` drop COLUMN `buy_limit_hero_count`;
ALTER TABLE `role` drop COLUMN `buy_limit_hero_yuanbao`;
CREATE TABLE `role_limit_hero` (
  `id` varchar(64) NOT NULL,
  `buy_count` int(11) NOT NULL,
  `buy_yuanbao` int(11) NOT NULL,
  `last_buy_date` datetime DEFAULT NULL,
  `today_buy_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150824兑换记录
CREATE TABLE `role_exchange_item` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `exchange_counts` int(11) DEFAULT NULL,
  `exchange_no` varchar(255) DEFAULT NULL,
  `exchange_out` bit(1) DEFAULT NULL,
  `role_id` varchar(64) DEFAULT NULL,
  `last_exchange_item_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2697A2332GB21D7Z` (`role_id`),
  CONSTRAINT `FK2697A2332GB21D7Z` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150827公会改名
ALTER TABLE `faction` ADD COLUMN `rename_date` datetime;

#20150909时空战役扫荡
ALTER TABLE `role_time_battle` ADD COLUMN `max_star` int(11) default 0;

#20150910秘境寻宝
CREATE TABLE `role_treasure` (
  `id` varchar(64) NOT NULL,
  `depart_date` datetime DEFAULT NULL,
  `hero_ids` varchar(255) DEFAULT NULL,
  `recommend_hero_json` varchar(500) DEFAULT NULL,
  `recommend_hero_num` int(11) DEFAULT NULL,
  `refresh_date` datetime DEFAULT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#20150907等级福利活动
CREATE TABLE `role_level_weal` (
   `id` varchar(64) NOT NULL,
   `accept_count` int(11) DEFAULT '0',
   `extra_accept_count` int(11) DEFAULT '0',
   `last_accept_date` datetime DEFAULT NULL,
   `update_time` datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 #20150914伙伴系统
 CREATE TABLE `role_partner` (
  `id` varchar(64) NOT NULL,
  `config` varchar(512) NOT NULL,
  `max_pos` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

#20150926寻宝增加队伍编号
ALTER TABLE `role_treasure` ADD COLUMN `group_num` int(11) default 1;
