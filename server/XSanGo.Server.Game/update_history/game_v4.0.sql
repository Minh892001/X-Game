#2016-09-08 神器
CREATE TABLE `role_artifact` (
  `id` varchar(64) NOT NULL,
  `use_hero_id` varchar(255) DEFAULT NULL,
  `artifact_id` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '0',
  `role_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;