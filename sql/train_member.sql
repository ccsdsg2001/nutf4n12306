/*
 Navicat Premium Data Transfer

 Source Server         : docker8
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : 140.210.221.183:3306
 Source Schema         : train_member

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 13/09/2023 18:56:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint NOT NULL COMMENT 'id',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobile_unique`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------

-- ----------------------------
-- Table structure for passenger
-- ----------------------------
DROP TABLE IF EXISTS `passenger`;
CREATE TABLE `passenger`  (
  `id` bigint NOT NULL COMMENT 'id',
  `member_id` bigint NOT NULL COMMENT '会员id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '旅客类型|枚举[PassengerTypeEnum]',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_id_index`(`member_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '乘车人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of passenger
-- ----------------------------

-- ----------------------------
-- Table structure for sk_token
-- ----------------------------
DROP TABLE IF EXISTS `sk_token`;
CREATE TABLE `sk_token`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `count` int NOT NULL COMMENT '令牌余量',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_unique`(`date` ASC, `train_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀令牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sk_token
-- ----------------------------

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `id` bigint NOT NULL COMMENT 'id',
  `member_id` bigint NOT NULL COMMENT '会员id',
  `passenger_id` bigint NOT NULL COMMENT '乘客id',
  `passenger_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乘客姓名',
  `train_date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `carriage_index` int NOT NULL COMMENT '箱序',
  `seat_row` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排号|01, 02',
  `seat_col` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列号|枚举[SeatColEnum]',
  `start_station` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站',
  `start_time` time NOT NULL COMMENT '出发时间',
  `end_station` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站',
  `end_time` time NOT NULL COMMENT '到站时间',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_id_index`(`member_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车票' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
