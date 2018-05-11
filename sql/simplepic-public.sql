/*
Navicat MySQL Data Transfer

Source Server         : 本地测试
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : simplepic-public

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-11 16:08:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for discuss
-- ----------------------------
DROP TABLE IF EXISTS `discuss`;
CREATE TABLE `discuss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `picture_upload_logs_id` bigint(20) DEFAULT NULL COMMENT '该评论属于哪个发现的内容',
  `from_open_id` varchar(255) DEFAULT NULL COMMENT '评论人的openid',
  `discuss_content` varchar(255) DEFAULT NULL COMMENT '评论内容',
  `discuss_time` datetime DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for picture_upload_logs
-- ----------------------------
DROP TABLE IF EXISTS `picture_upload_logs`;
CREATE TABLE `picture_upload_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL COMMENT '用户唯一标识',
  `upload_picture_url` varchar(255) DEFAULT NULL COMMENT '上传原图在服务器的地址',
  `simplify_picture_url` varchar(255) DEFAULT NULL COMMENT '简化后的图片在服务器的地址',
  `thumbnail_picture_url` varchar(255) DEFAULT NULL COMMENT '简化图的缩略图在服务器的地址',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `like_number` int(11) DEFAULT NULL COMMENT '点赞数',
  `share_number` int(11) DEFAULT NULL,
  `is_delete` int(11) DEFAULT NULL COMMENT '是否删除的标记0正常,1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for reply_discuss
-- ----------------------------
DROP TABLE IF EXISTS `reply_discuss`;
CREATE TABLE `reply_discuss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL COMMENT '回复者openid',
  `discuss_id` int(11) DEFAULT NULL COMMENT '回复的是哪条评论',
  `reply_content` varchar(255) DEFAULT NULL COMMENT '回复的内容',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_like_picture_logs
-- ----------------------------
DROP TABLE IF EXISTS `user_like_picture_logs`;
CREATE TABLE `user_like_picture_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL COMMENT '当前用户',
  `picture_upload_logs_id` bigint(20) DEFAULT NULL COMMENT '上传记录id',
  `like_time` datetime DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_login_logs
-- ----------------------------
DROP TABLE IF EXISTS `user_login_logs`;
CREATE TABLE `user_login_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) DEFAULT NULL COMMENT '对应用户的openid',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subscribe` int(11) DEFAULT NULL COMMENT '用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。',
  `openid` varchar(255) DEFAULT NULL COMMENT '用户的标识，对当前公众号唯一',
  `nickname` varchar(255) DEFAULT NULL COMMENT '用户的昵称',
  `sex` int(11) DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `city` varchar(255) DEFAULT NULL COMMENT '用户所在城市',
  `country` varchar(255) DEFAULT NULL COMMENT '用户所在国家',
  `province` varchar(255) DEFAULT NULL COMMENT '用户所在省份',
  `language` varchar(255) DEFAULT NULL COMMENT '用户的语言，简体中文为zh_CN',
  `headimgurl` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `subscribe_time` datetime DEFAULT NULL COMMENT '用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间',
  `unionid` varchar(255) DEFAULT NULL COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。',
  `remark` varchar(255) DEFAULT NULL COMMENT '公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注',
  `groupid` int(11) DEFAULT NULL COMMENT '用户所在的分组ID（兼容旧的用户分组接口）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
