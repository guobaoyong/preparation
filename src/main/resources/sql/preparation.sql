/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : preparation

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 17/09/2019 17:39:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_arc_type
-- ----------------------------
DROP TABLE IF EXISTS `t_arc_type`;
CREATE TABLE `t_arc_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_arc_type
-- ----------------------------
INSERT INTO `t_arc_type` VALUES (20, 'Java技术', 'Java技术相关', 1);
INSERT INTO `t_arc_type` VALUES (21, '数据库技术', '数据库技术相关', 2);
INSERT INTO `t_arc_type` VALUES (22, 'Web前端技术', 'Web前端技术相关', 3);
INSERT INTO `t_arc_type` VALUES (23, 'J2EE技术', 'J2EE技术相关', 4);
INSERT INTO `t_arc_type` VALUES (24, '分布式微服技术', '分布式微服技术相关', 5);
INSERT INTO `t_arc_type` VALUES (25, '移动APP开发技术', '移动APP开发技术相关', 6);
INSERT INTO `t_arc_type` VALUES (26, '微信小程序开发', '微信小程序开发相关', 7);
INSERT INTO `t_arc_type` VALUES (27, '服务器技术', '服务器技术相关', 8);
INSERT INTO `t_arc_type` VALUES (28, '人工智能', '人工智能', 9);
INSERT INTO `t_arc_type` VALUES (29, '数据挖掘', '数据挖掘', 10);
INSERT INTO `t_arc_type` VALUES (30, '大数据云计算', '大数据云计算', 11);
INSERT INTO `t_arc_type` VALUES (31, '区块链', '区块链', 12);
INSERT INTO `t_arc_type` VALUES (32, '机器学习', '机器学习', 13);
INSERT INTO `t_arc_type` VALUES (33, '算法', '算法', 14);
INSERT INTO `t_arc_type` VALUES (34, 'Java架构', 'Java架构', 8);
INSERT INTO `t_arc_type` VALUES (35, '其他', '其他', 15);
INSERT INTO `t_arc_type` VALUES (36, '软件测试', '软件测试', 14);

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `check_date` datetime(0) NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `download1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_hot` bit(1) NOT NULL,
  `is_useful` bit(1) NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password1` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `points` int(11) NULL DEFAULT NULL,
  `publish_date` datetime(0) NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` int(11) NULL DEFAULT NULL,
  `view` int(11) NULL DEFAULT NULL,
  `type_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKjqtx09lsbkwtq5dgmt9sw64o0`(`type_id`) USING BTREE,
  INDEX `FKsgqpqfl0p7olcr7694a3pjl0q`(`user_id`) USING BTREE,
  CONSTRAINT `FKjqtx09lsbkwtq5dgmt9sw64o0` FOREIGN KEY (`type_id`) REFERENCES `t_arc_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKsgqpqfl0p7olcr7694a3pjl0q` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article
-- ----------------------------
INSERT INTO `t_article` VALUES (1, NULL, '测试内容1', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题1', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (2, NULL, '测试内容2', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题2', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (3, NULL, '测试内容3', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题3', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (4, NULL, '测试内容4', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题4', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (5, NULL, '测试内容5', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题5', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (6, NULL, '测试内容6', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题6', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (7, NULL, '测试内容7', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题7', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (8, NULL, '测试内容8', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题8', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (9, NULL, '测试内容9', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题9', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (10, NULL, '测试内容10', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题10', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (11, NULL, '测试内容11', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题11', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (12, NULL, '测试内容12', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题12', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (13, NULL, '测试内容13', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题13', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (14, NULL, '测试内容14', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题14', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (15, NULL, '测试内容15', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题15', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (16, NULL, '测试内容16', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题16', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (17, NULL, '测试内容17', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题17', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (18, NULL, '测试内容18', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题18', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (19, NULL, '测试内容19', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题19', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (20, NULL, '测试内容20', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题20', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (21, NULL, '测试内容21', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题21', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (22, NULL, '测试内容22', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题22', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (23, NULL, '测试内容23', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题23', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (24, NULL, '测试内容24', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题24', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (25, NULL, '测试内容25', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题25', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (26, NULL, '测试内容26', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题26', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (27, NULL, '测试内容27', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题27', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (28, NULL, '测试内容28', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题28', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (29, NULL, '测试内容29', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题29', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (30, NULL, '测试内容30', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题30', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (31, NULL, '测试内容31', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题31', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (32, NULL, '测试内容32', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题32', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (33, NULL, '测试内容33', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题33', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (34, NULL, '测试内容34', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题34', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (35, NULL, '测试内容35', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题35', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (36, NULL, '测试内容36', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题36', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (37, NULL, '测试内容37', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题37', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (38, NULL, '测试内容38', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题38', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (39, NULL, '测试内容39', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题39', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (40, NULL, '测试内容40', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题40', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (41, NULL, '测试内容41', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题41', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (42, NULL, '测试内容42', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题42', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (43, NULL, '测试内容43', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题43', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (44, NULL, '测试内容44', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题44', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (45, NULL, '测试内容45', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题45', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (46, NULL, '测试内容46', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题46', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (47, NULL, '测试内容47', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题47', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (48, NULL, '测试内容48', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题48', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (49, NULL, '测试内容49', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题49', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (50, NULL, '测试内容50', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题50', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (51, NULL, '测试内容51', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题51', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (52, NULL, '测试内容52', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题52', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (53, NULL, '测试内容53', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题53', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (54, NULL, '测试内容54', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题54', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (55, NULL, '测试内容55', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题55', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (56, NULL, '测试内容56', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题56', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (57, NULL, '测试内容57', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题57', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (58, NULL, '测试内容58', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题58', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (59, NULL, '测试内容59', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题59', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (60, NULL, '测试内容60', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题60', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (61, NULL, '测试内容61', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题61', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (62, NULL, '测试内容62', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题62', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (63, NULL, '测试内容63', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题63', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (64, NULL, '测试内容64', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题64', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (65, NULL, '测试内容65', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题65', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (66, NULL, '测试内容66', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题66', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (67, NULL, '测试内容67', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题67', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (68, NULL, '测试内容68', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题68', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (69, NULL, '测试内容69', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题69', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (70, NULL, '测试内容70', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题70', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (71, NULL, '测试内容71', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题71', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (72, NULL, '测试内容72', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题72', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (73, NULL, '测试内容73', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题73', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (74, NULL, '测试内容74', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题74', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (75, NULL, '测试内容75', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题75', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (76, NULL, '测试内容76', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题76', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (77, NULL, '测试内容77', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题77', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (78, NULL, '测试内容78', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题78', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (79, NULL, '测试内容79', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题79', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (80, NULL, '测试内容80', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题80', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (81, NULL, '测试内容81', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题81', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (82, NULL, '测试内容82', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题82', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (83, NULL, '测试内容83', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题83', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (84, NULL, '测试内容84', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题84', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (85, NULL, '测试内容85', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题85', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (86, NULL, '测试内容86', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题86', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (87, NULL, '测试内容87', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题87', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (88, NULL, '测试内容88', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题88', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (89, NULL, '测试内容89', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题89', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (90, NULL, '测试内容90', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题90', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (91, NULL, '测试内容91', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题91', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (92, NULL, '测试内容92', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题92', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (93, NULL, '测试内容93', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题93', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (94, NULL, '测试内容94', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题94', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (95, NULL, '测试内容95', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题95', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (96, NULL, '测试内容96', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题96', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (97, NULL, '测试内容97', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题97', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (98, NULL, '测试内容98', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题98', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (99, NULL, '测试内容99', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'0', b'0', '测试标题99', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 20, 1);
INSERT INTO `t_article` VALUES (100, NULL, '测试内容100', 'https://pan.baidu.com/s/1nJNnAfdyi4g_72N18DS6hQ', b'1', b'0', '测试标题100', '1234', 10, '2018-09-11 05:55:44', '', 2, 100, 21, 1);

-- ----------------------------
-- Table structure for t_link
-- ----------------------------
DROP TABLE IF EXISTS `t_link`;
CREATE TABLE `t_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_link
-- ----------------------------
INSERT INTO `t_link` VALUES (1, 'Java1234', 1, 'http://www.java1234.com');
INSERT INTO `t_link` VALUES (2, 'Java1234博客', 2, 'http://blog.java1234.com');
INSERT INTO `t_link` VALUES (4, 'Java源码资源分享网', 3, 'http://www.javafxw.com');
INSERT INTO `t_link` VALUES (5, 'java资料源码分享网', 4, 'http://www.downziyuan.cn/');
INSERT INTO `t_link` VALUES (6, 'IT口袋网资源下载', 6, 'http://www.itkoudai.com/');
INSERT INTO `t_link` VALUES (7, 'Java分享网', 7, 'http://www.javaxz.com');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_off` bit(1) NOT NULL,
  `is_vip` bit(1) NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `points` int(11) NULL DEFAULT NULL,
  `register_date` datetime(0) NULL DEFAULT NULL,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '123@qq.com', 'das.jpg', b'0', b'0', '123', 22, '2018-10-15 23:26:20', '会员', 'jack');

SET FOREIGN_KEY_CHECKS = 1;
