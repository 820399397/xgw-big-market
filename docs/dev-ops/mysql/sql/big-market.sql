/*
 Navicat Premium Data Transfer

 Source Server         : xgw
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 192.168.56.100:13306
 Source Schema         : big-market

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 29/06/2024 12:23:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for strategy
-- ----------------------------
DROP TABLE IF EXISTS `strategy`;
CREATE TABLE `strategy`  (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                             `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
                             `strategy_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抽象策略描述',
                             `rule_models` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则模型,strategy_rule配置规则记录',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '策略总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy
-- ----------------------------
INSERT INTO `strategy` VALUES (1, 100001, '抽奖策略', 'rule_weight,rule_backlist', '2024-06-29 11:31:19', '2024-06-29 12:15:39');

-- ----------------------------
-- Table structure for strategy_award
-- ----------------------------
DROP TABLE IF EXISTS `strategy_award`;
CREATE TABLE `strategy_award`  (
                                   `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                   `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
                                   `award_id` bigint NOT NULL COMMENT '抽奖奖品ID',
                                   `award_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抽奖奖品标题',
                                   `award_subtitle` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抽奖奖品副标题',
                                   `award_count` int NOT NULL DEFAULT 0 COMMENT '奖品库存总量',
                                   `award_count_surplus` int NOT NULL DEFAULT 0 COMMENT '奖品库存剩余量',
                                   `award_rate` decimal(6, 4) NOT NULL COMMENT '奖品中奖概率',
                                   `rule_models` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则模型,strategy_rule配置规则记录',
                                   `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `idx_strategy_id_award_id`(`strategy_id` ASC, `award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抽奖策略奖品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_award
-- ----------------------------
INSERT INTO `strategy_award` VALUES (1, 100001, 101, '随机积分', NULL, 80000, 80000, 80.0000, 'rule_random,rule_luck_award', 1, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (2, 100001, 102, '5次使用', NULL, 10000, 10000, 10.0000, 'rule_luck_award', 2, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (3, 100001, 103, '10次使用', NULL, 5000, 5000, 5.0000, 'rule_luck_award', 3, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (4, 100001, 104, '20次使用', NULL, 4000, 4000, 4.0000, 'rule_luck_award', 4, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (5, 100001, 105, '增加gpt-4对话模型', NULL, 600, 600, 0.6000, 'rule_luck_award', 5, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (6, 100001, 106, '增加dall-e-2画图模型', NULL, 200, 200, 0.2000, 'rule_luck_award', 6, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (7, 100001, 107, '增加dall-e-3画图模型', '抽奖1次后解锁', 200, 200, 0.2000, 'rule_lock,rule_luck_award', 7, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (8, 100001, 108, '增加100次使用', '抽奖2次后解锁', 199, 199, 0.1999, 'rule_lock,rule_luck_award', 8, '2024-06-29 12:21:09', '2024-06-29 12:21:09');
INSERT INTO `strategy_award` VALUES (9, 100001, 109, '解锁全部模型', '抽奖6次后解锁', 1, 1, 0.0001, 'rule_lock,rule_luck_award', 9, '2024-06-29 12:21:09', '2024-06-29 12:21:09');

-- ----------------------------
-- Table structure for strategy_rule
-- ----------------------------
DROP TABLE IF EXISTS `strategy_rule`;
CREATE TABLE `strategy_rule`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
                                  `award_id` bigint NULL DEFAULT NULL COMMENT '抽奖奖品ID【规则类型为策略，则不需要奖品ID】',
                                  `rule_type` tinyint NOT NULL DEFAULT 0 COMMENT '抽奖规则类型；1-策略规则、2-奖品规则',
                                  `rule_model` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】',
                                  `rule_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抽奖规则比值',
                                  `rule_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抽奖规则描述',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_strategy_id_award_id`(`strategy_id` ASC, `award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抽奖策略规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_rule
-- ----------------------------
INSERT INTO `strategy_rule` VALUES (1, 100001, 101, 2, 'rule_random', '1,1000', '随机积分策略', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (2, 100001, 107, 2, 'rule_lock', '1', '抽奖1次后解锁', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (3, 100001, 108, 2, 'rule_lock', '2', '抽奖2次后解锁', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (4, 100001, 109, 2, 'rule_lock', '6', '抽奖6次后解锁', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (5, 100001, 107, 2, 'rule_luck_award', '1,100', '兜底奖品100以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (6, 100001, 108, 2, 'rule_luck_award', '1,100', '兜底奖品100以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (7, 100001, 101, 2, 'rule_luck_award', '1,10', '兜底奖品10以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (8, 100001, 102, 2, 'rule_luck_award', '1,20', '兜底奖品20以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (9, 100001, 103, 2, 'rule_luck_award', '1,30', '兜底奖品30以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (10, 100001, 104, 2, 'rule_luck_award', '1,40', '兜底奖品40以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (11, 100001, 105, 2, 'rule_luck_award', '1,50', '兜底奖品50以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (12, 100001, 106, 2, 'rule_luck_award', '1,60', '兜底奖品60以内随机积分', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (13, 100001, NULL, 1, 'rule_weight', '6000,102,103,104,105,106,107,108,109', '消耗6000分，必中奖范围', '2024-06-29 12:22:37', '2024-06-29 12:22:37');
INSERT INTO `strategy_rule` VALUES (14, 100001, NULL, 1, 'rule_blacklist', '1', '黑名单抽奖，积分兜底', '2024-06-29 12:22:37', '2024-06-29 12:22:37');


CREATE TABLE `award` (
                         `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                         `award_id` bigint NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
                         `award_key` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '奖品对接标识 - 每一个都是一个对应的发奖策略',
                         `award_config` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '奖品配置信息',
                         `award_desc` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '奖品内容描述',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='奖品表';

INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (1, 101, 'user_credit_random', '1,100', '用户积分【优先透彻规则范围，如果没有则走配置】', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (2, 102, 'openai_use_count', '5', 'OpenAI 增加使用次数', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (3, 103, 'openai_use_count', '10', 'OpenAI 增加使用次数', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (4, 104, 'openai_use_count', '20', 'OpenAI 增加使用次数', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (5, 105, 'openai_model', 'gpt-4', 'OpenAI 增加模型', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (6, 106, 'openai_model', 'dall-e-2', 'OpenAI 增加模型', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (7, 107, 'openai_model', 'dall-e-3', 'OpenAI 增加模型', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (8, 108, 'openai_use_count', '100', 'OpenAI 增加使用次数', '2024-07-01 10:05:45', '2024-07-01 10:05:45');
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`, `create_time`, `update_time`) VALUES (9, 109, 'openai_model', 'gpt-4,dall-e-2,dall-e-3', 'OpenAI 增加模型', '2024-07-01 10:05:45', '2024-07-01 10:05:45');


SET FOREIGN_KEY_CHECKS = 1;
