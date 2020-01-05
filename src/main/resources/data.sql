
-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) primary key auto_increment,
  `current_progress` int(11),
  `target_progress` int(11),
  `start_date` date,
  `last_modified_date` date,
  `name` varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` (current_progress, target_progress, start_date, last_modified_date, name)
  VALUES ('155', '715', '2019-01-05', '2019-01-14', '任务一');
INSERT INTO `task` VALUES ('38', '242', '973', '2019-02-06', '2019-06-26', '任务二');
INSERT INTO `task` VALUES ('39', '315', '477', '2019-06-01', '2019-06-01', '任务三');
INSERT INTO `task` VALUES ('40', '68', '422', '2019-06-26', '2019-07-19', '任务四');
INSERT INTO `task` VALUES ('41', '106', '846', '2019-06-26', '2019-07-07', '任务五');
INSERT INTO `task` VALUES ('42', '13', '487', '2019-06-26', '2019-06-26', '任务六');
INSERT INTO `task` VALUES ('43', '21', '743', '2019-06-27', '2019-06-27', '任务七');

DROP TABLE IF EXISTS `diary`;
CREATE TABLE `diary` (
  `id` int(11) primary key auto_increment,
  `title` varchar(50),
  `date` date,
  `text` varchar(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of diary
-- ----------------------------
INSERT INTO `diary` (title, date, text) VALUES ('测试一', '2018-06-12', '测试一文本');
INSERT INTO `diary` (title, date, text) VALUES ('测试二', '2018-06-12', '测试二文本');
INSERT INTO `diary` (title, date, text) VALUES ('card', '2019-01-07', '');
INSERT INTO `diary` (title, date, text)VALUES  ('card', '2019-01-13', '');
INSERT INTO `diary` (title, date, text)VALUES  ('card', '2019-01-15', '');
INSERT INTO `diary` (title, date, text)VALUES  ('card', '2019-01-20', '');
