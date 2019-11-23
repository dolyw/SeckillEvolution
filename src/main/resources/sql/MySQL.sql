--- 删除数据库
drop database seckill;
--- 创建数据库
create database seckill;
--- 使用数据库
use seckill;
--- 创建库存表
DROP TABLE IF EXISTS `t_seckill_stock`;
CREATE TABLE `t_seckill_stock` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `name` varchar(50) NOT NULL DEFAULT 'OnePlus 7 Pro' COMMENT '名称',
  `count` int(11) NOT NULL COMMENT '库存',
  `sale` int(11) NOT NULL COMMENT '已售',
  `version` int(11) NOT NULL COMMENT '乐观锁，版本号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='库存表';
--- 插入一条商品，初始化10个库存
INSERT INTO `t_seckill_stock` (`count`, `sale`, `version`) VALUES ('10', '0', '0');
--- 创建库存订单表
DROP TABLE IF EXISTS `t_seckill_stock_order`;
CREATE TABLE `t_seckill_stock_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `stock_id` int(11) NOT NULL COMMENT '库存ID',
  `name` varchar(30) NOT NULL DEFAULT 'OnePlus 7 Pro' COMMENT '商品名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='库存订单表';
