CREATE DATABASE IF NOT EXISTS ispider;
DROP TABLE IF EXISTS `phone`;
CREATE TABLE `phone` (
  `id` varchar(30) CHARACTER SET armscii8 NOT NULL COMMENT '商品id',
  `source` varchar(30) NOT NULL COMMENT '商品来源，如jd suning gome等',
  `brand` varchar(30) DEFAULT NULL COMMENT '手机品牌',
  `title` varchar(255) DEFAULT NULL COMMENT '商品页面的手机标题',
  `price` float(10,2) DEFAULT NULL COMMENT '手机价格',
  `comment_count` varchar(30) DEFAULT NULL COMMENT '手机评论',
  `good_rate` float(10,3) DEFAULT NULL COMMENT '好评率',
  `url` varchar(500) DEFAULT NULL COMMENT '手机详细信息地址',
  `img_url` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `main` varchar(500) DEFAULT NULL COMMENT '主体，产品名称',
  `battery` varchar(500) DEFAULT NULL COMMENT '电池信息',
  `interface` varchar(500) DEFAULT NULL COMMENT '数据接口',
  `network` varchar(500) DEFAULT NULL COMMENT '网络支持',
  `operating_system` varchar(500) DEFAULT NULL COMMENT '操作系统',
  `basic_info` varchar(500) DEFAULT NULL COMMENT '基本信息',
  `camera` varchar(500) DEFAULT NULL COMMENT '摄像头',
  `screen` varchar(500) DEFAULT NULL COMMENT '屏幕',
  PRIMARY KEY (`id`,`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT * FROM phone WHERE title LIKE '%RedMi%';