DROP TABLE IF EXISTS `cross_rank`;
CREATE TABLE `cross_rank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `fail_num` int(11) NOT NULL,
  `pvp_view` text,
  `role_id` varchar(255) DEFAULT NULL,
  `role_view` varchar(1000) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `toast_num` int(11) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `win_num` int(11) NOT NULL,
  `into_stage` int(11) NOT NULL,
  `cross_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=472 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cross_id` int(11) NOT NULL,
  `group_num` int(11) NOT NULL,
  `movie_view` mediumtext,
  `order_num` int(11) NOT NULL,
  `role_view1` varchar(1000) DEFAULT NULL,
  `role_view2` varchar(1000) DEFAULT NULL,
  `stage` int(11) NOT NULL,
  `win_role_view` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=467 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cross_stage`;
CREATE TABLE `cross_stage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cross_id` int(11) NOT NULL,
  `stage` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cross_log`;
CREATE TABLE `cross_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `champion_id` varchar(255) DEFAULT NULL,
  `champion_server_id` int(11) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `period_num` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

#20160128 比武大会老积分不发奖处理
CREATE TABLE `score_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(255) DEFAULT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

insert into score_log(role_id,score) select role_id,score from cross_rank;

#20160218 比武大会淘汰赛重打
update cross_rank set into_stage = 0,toast_num = 0;
delete from `schedule` where stage < 32;
update `schedule` set movie_view = '[]',win_role_view = null where role_view2 !='';
delete from cross_stage;

#20160315 淘汰赛记录历史战力
ALTER TABLE `schedule` ADD COLUMN `battle_power1` int(11) default 0;
ALTER TABLE `schedule` ADD COLUMN `battle_power2` int(11) default 0;

#清空上届历史积分
DELETE FROM `score_log`;