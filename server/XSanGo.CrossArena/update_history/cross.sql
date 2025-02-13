CREATE TABLE `cross_arena_rank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attack_fight_sum` int(11) NOT NULL,
  `attack_win_sum` int(11) NOT NULL,
  `formation_view` text,
  `guard_fight_sum` int(11) NOT NULL,
  `guard_win_sum` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `rival_rank` varchar(2000) DEFAULT NULL,
  `role_id` varchar(255) DEFAULT NULL,
  `signature` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cross_arena_rank_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=852 DEFAULT CHARSET=utf8;

CREATE TABLE `cross_arena_movie` (
  `id` varchar(255) NOT NULL,
  `datetime` datetime DEFAULT NULL,
  `fight_movie` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `cross_arena_rank` ADD COLUMN `range_Id` INT(11) DEFAULT 1;