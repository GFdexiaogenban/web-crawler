### 数据库代码：

#### house_detail

```mysql
CREATE TABLE `crawler`.`house_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `house_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '房屋名称',
  `house_alias` varchar(100) CHARACTER SET utf8 NULL COMMENT '房屋别名',
  `price` varchar(50) CHARACTER SET utf8 NULL COMMENT '价格',
  `property_type` varchar(50) CHARACTER SET utf8 NULL COMMENT '物业类型',
  `property_company` varchar(100) CHARACTER SET utf8 NULL COMMENT '物业公司',
  `decoration_status` varchar(100) CHARACTER SET utf8 NULL COMMENT '装修情况',
  `opening_time_place` varchar(100) CHARACTER SET utf8 NULL COMMENT '开盘时间-地点',
  `sales_address` varchar(100) CHARACTER SET utf8 NULL COMMENT '售楼地址',
  `location` varchar(100) CHARACTER SET utf8 NULL COMMENT '项目位置',
  `project_features` varchar(255) CHARACTER SET utf8 NULL COMMENT '项目特色',
  `check_in_time` varchar(100) CHARACTER SET utf8 NULL COMMENT '入住时间',
  `property_fee` varchar(50) CHARACTER SET utf8 NULL COMMENT '物业费',
  `term_of_property_right` varchar(100) CHARACTER SET utf8 NULL COMMENT '产权年限',
  `pre_sale_permit` text CHARACTER SET utf8 NULL COMMENT '预售许可证',
  `issuing_time` text CHARACTER SET utf8 NULL COMMENT '发证时间',
  `building_number` text CHARACTER SET utf8 NULL COMMENT '楼栋号',
  `architectural_planning` text CHARACTER SET utf8 NULL COMMENT '建筑规划',
  `project_brief` text CHARACTER SET utf8 NULL COMMENT '项目简介',
  `surrounding_facilities` text CHARACTER SET utf8 NULL COMMENT '周边配套',
  `community_supporting` text CHARACTER SET utf8 NULL COMMENT '小区配套',
  `traffic_situation` text CHARACTER SET utf8 NULL COMMENT '交通情况',
  `floor_condition` text CHARACTER SET utf8 NULL COMMENT '楼层状况',
  `delivery_standard` text CHARACTER SET utf8 NULL COMMENT '交付标准',
  `other_information` text CHARACTER SET utf8 NULL COMMENT '其他信息',
  `url` varchar(255) CHARACTER SET utf8 NULL COMMENT '当前网址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='房屋信息';
```

#### house_trend_info

```mysql
CREATE TABLE `crawler`.`house_trend_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
  `house_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '房屋名称',
  `trend_title` varchar(100) CHARACTER SET utf8 NULL COMMENT '动态标题',
  `trend_time` varchar(100) CHARACTER SET utf8 NULL COMMENT '动态发布时间',
  `contents` text CHARACTER SET utf8 NULL COMMENT '主要内容',
  `directory` varchar(100) CHARACTER SET utf8 NULL COMMENT '图片保存路径',
  `trend_url` varchar(255) CHARACTER SET utf8 NULL COMMENT '当前网址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='动态信息';
```

#### house_type_pic

```mysql
CREATE TABLE `crawler`.`house_type_pic`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
  `house_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '房屋名称',
  `house_type` varchar(100) CHARACTER SET utf8 NULL COMMENT '房屋类型',
  `type_name` varchar(100) CHARACTER SET utf8 NULL COMMENT '户型名称',
  `price` varchar(100) CHARACTER SET utf8 NULL COMMENT '价格',
  `building_area` varchar(100) CHARACTER SET utf8 NULL COMMENT '建筑面积',
  `house_area` varchar(100) CHARACTER SET utf8 NULL COMMENT '套内面积',
  `pic_directory` varchar(100) CHARACTER SET utf8 NULL COMMENT '图片保存路径',
  `pic_num` bigint NULL COMMENT '图片总数',
  `url` varchar(255) CHARACTER SET utf8 NULL COMMENT '当前页面网址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='户型图';
```

#### house_photo

```mysql
CREATE TABLE `crawler`.`house_photo`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
  `house_name` varchar(100) CHARACTER SET utf8 NULL COMMENT '房屋名字',
  `pic_type` varchar(100) CHARACTER SET utf8 NULL COMMENT '图片类型',
  `pic_name` varchar(100) CHARACTER SET utf8 NULL COMMENT '图片名字',
  `pic_directory` varchar(100) CHARACTER SET utf8 NULL COMMENT '图片存放路径',
  `url` varchar(255) CHARACTER SET utf8 NULL COMMENT '对应网址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='房屋图片';
```

`https://github.com/GFdexiaogenban/web-crawler.git`

`git@github.com:GFdexiaogenban/web-crawler.git`

在D盘下新建一个文件夹`pic`用于存放照片文件