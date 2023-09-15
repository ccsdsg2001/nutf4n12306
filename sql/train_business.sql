/*
 Navicat Premium Data Transfer

 Source Server         : docker8
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : 140.210.221.183:3306
 Source Schema         : train_business

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 13/09/2023 18:53:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for confirm_order
-- ----------------------------
DROP TABLE IF EXISTS `confirm_order`;
CREATE TABLE `confirm_order`  (
  `id` bigint NOT NULL COMMENT 'id',
  `member_id` bigint NOT NULL COMMENT '会员id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站',
  `daily_train_ticket_id` bigint NOT NULL COMMENT '余票ID',
  `tickets` json NOT NULL COMMENT '车票',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态|枚举[ConfirmOrderStatusEnum]',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `date_train_code_index`(`date` ASC, `train_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '确认订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of confirm_order
-- ----------------------------

-- ----------------------------
-- Table structure for daily_train
-- ----------------------------
DROP TABLE IF EXISTS `daily_train`;
CREATE TABLE `daily_train`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次类型|枚举[TrainTypeEnum]',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站',
  `start_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站拼音',
  `start_time` time NOT NULL COMMENT '出发时间',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站',
  `end_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站拼音',
  `end_time` time NOT NULL COMMENT '到站时间',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_code_unique`(`date` ASC, `code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日车次' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train
-- ----------------------------

-- ----------------------------
-- Table structure for daily_train_carriage
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_carriage`;
CREATE TABLE `daily_train_carriage`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '箱序',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `seat_count` int NOT NULL COMMENT '座位数',
  `row_count` int NOT NULL COMMENT '排数',
  `col_count` int NOT NULL COMMENT '列数',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_index_unique`(`date` ASC, `train_code` ASC, `index` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日车厢' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_carriage
-- ----------------------------

-- ----------------------------
-- Table structure for daily_train_seat
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_seat`;
CREATE TABLE `daily_train_seat`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `carriage_index` int NOT NULL COMMENT '箱序',
  `row` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排号|01, 02',
  `col` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列号|枚举[SeatColEnum]',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `carriage_seat_index` int NOT NULL COMMENT '同车箱座序',
  `sell` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日座位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_seat
-- ----------------------------

-- ----------------------------
-- Table structure for daily_train_station
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_station`;
CREATE TABLE `daily_train_station`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '站序|第一站是0',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名',
  `name_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音',
  `in_time` time NULL DEFAULT NULL COMMENT '进站时间',
  `out_time` time NULL DEFAULT NULL COMMENT '出站时间',
  `stop_time` time NULL DEFAULT NULL COMMENT '停站时长',
  `km` decimal(8, 2) NOT NULL COMMENT '里程（公里）|从上一站到本站的距离',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_index_unique`(`date` ASC, `train_code` ASC, `index` ASC) USING BTREE,
  UNIQUE INDEX `date_train_code_name_unique`(`date` ASC, `train_code` ASC, `name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日车站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_station
-- ----------------------------

-- ----------------------------
-- Table structure for daily_train_ticket
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_ticket`;
CREATE TABLE `daily_train_ticket`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站',
  `start_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站拼音',
  `start_time` time NOT NULL COMMENT '出发时间',
  `start_index` int NOT NULL COMMENT '出发站序|本站是整个车次的第几站',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站',
  `end_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站拼音',
  `end_time` time NOT NULL COMMENT '到站时间',
  `end_index` int NOT NULL COMMENT '到站站序|本站是整个车次的第几站',
  `ydz` int NOT NULL COMMENT '一等座余票',
  `ydz_price` decimal(8, 2) NOT NULL COMMENT '一等座票价',
  `edz` int NOT NULL COMMENT '二等座余票',
  `edz_price` decimal(8, 2) NOT NULL COMMENT '二等座票价',
  `rw` int NOT NULL COMMENT '软卧余票',
  `rw_price` decimal(8, 2) NOT NULL COMMENT '软卧票价',
  `yw` int NOT NULL COMMENT '硬卧余票',
  `yw_price` decimal(8, 2) NOT NULL COMMENT '硬卧票价',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_start_end_unique`(`date` ASC, `train_code` ASC, `start` ASC, `end` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '余票信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_ticket
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
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名',
  `name_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音',
  `name_py` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音首字母',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of station
-- ----------------------------

-- ----------------------------
-- Table structure for train
-- ----------------------------
DROP TABLE IF EXISTS `train`;
CREATE TABLE `train`  (
  `id` bigint NOT NULL COMMENT 'id',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次类型|枚举[TrainTypeEnum]',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站',
  `start_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站拼音',
  `start_time` time NOT NULL COMMENT '出发时间',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站',
  `end_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站拼音',
  `end_time` time NOT NULL COMMENT '到站时间',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_unique`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车次' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train
-- ----------------------------

-- ----------------------------
-- Table structure for train_carriage
-- ----------------------------
DROP TABLE IF EXISTS `train_carriage`;
CREATE TABLE `train_carriage`  (
  `id` bigint NOT NULL COMMENT 'id',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '厢号',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `seat_count` int NOT NULL COMMENT '座位数',
  `row_count` int NOT NULL COMMENT '排数',
  `col_count` int NOT NULL COMMENT '列数',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `train_code_index_unique`(`train_code` ASC, `index` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '火车车厢' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train_carriage
-- ----------------------------

-- ----------------------------
-- Table structure for train_seat
-- ----------------------------
DROP TABLE IF EXISTS `train_seat`;
CREATE TABLE `train_seat`  (
  `id` bigint NOT NULL COMMENT 'id',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `carriage_index` int NOT NULL COMMENT '厢序',
  `row` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排号|01, 02',
  `col` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列号|枚举[SeatColEnum]',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `carriage_seat_index` int NOT NULL COMMENT '同车厢座序',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '座位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train_seat
-- ----------------------------

-- ----------------------------
-- Table structure for train_station
-- ----------------------------
DROP TABLE IF EXISTS `train_station`;
CREATE TABLE `train_station`  (
  `id` bigint NOT NULL COMMENT 'id',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '站序',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名',
  `name_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音',
  `in_time` time NULL DEFAULT NULL COMMENT '进站时间',
  `out_time` time NULL DEFAULT NULL COMMENT '出站时间',
  `stop_time` time NULL DEFAULT NULL COMMENT '停站时长',
  `km` decimal(8, 2) NOT NULL COMMENT '里程（公里）|从上一站到本站的距离',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `train_code_index_unique`(`train_code` ASC, `index` ASC) USING BTREE,
  UNIQUE INDEX `train_code_name_unique`(`train_code` ASC, `name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '火车车站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train_station
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
