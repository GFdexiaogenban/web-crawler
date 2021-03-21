### 数据库内容：

| 名称                   | 介绍          | 类型         | html代码                                                     |
| ---------------------- | ------------- | ------------ | ------------------------------------------------------------ |
| id                     | 编号          | int          | 无                                                           |
| house_name             | 房屋名称      | varchar(100) | div.(title box) div.t h1                                     |
| house_alias            | 房屋别名      | varchar(100) | div.(title box) div.t p                                      |
| price                  | 价格          | varchar(50)  | div.house-left div.baseInfo li span() get(0)                 |
| unit                   | 价格单位      | varchar(20)  |                                                              |
| property_type          | 物业类型      | varchar(50)  | div.house-left div.baseInfo li span() get(2)                 |
| property_company       | 物业公司      | varchar(100) | div.house-left div.baseInfo li span() get(4)                 |
| decoration_status      | 装修情况      | varchar(100) | div.house-left div.baseInfo li span() get(6)                 |
| opening_time_place     | 开盘时间-地点 | varchar(100) | div.house-left div.baseInfo li div.wuzhengFount              |
| sales_address          | 售楼地址      | varchar(100) | div.house-left div.baseInfo li.full get(1)                   |
| location               | 项目位置      | varchar(100) | div.house-left div.baseInfo li.full get(2)                   |
| project_features       | 项目特色      | varchar(255) | div.house-left div.baseInfo li span() get(1)                 |
| check_in_time          | 入住时间      | varchar(100) | div.house-left div.baseInfo li span() get(3)                 |
| property_fee           | 物业费        | varchar(50)  | div.house-left div.baseInfo li span() get(5)                 |
| property_unit          | 物业费单位    | varchar(20)  |                                                              |
| term_of_property_right | 产权年限      | int(0)       | div.house-left div.baseInfo li span() get(7)                 |
| pre_sale_permit        | 预售许可证    | text         | div.house-left div.baseInfo table.licence-table tbody td get(0) |
| issuing_time           | 发证时间      | text         | div.house-left div.baseInfo table.licence-table tbody td get(1) |
| building_number        | 楼栋号        | text         | div.house-left div.baseInfo table.licence-table tbody td get(2) |
| architectural_planning | 建筑规划      | text         | (jsoup)div.house-left div.baseInfo ul[last()-1]              |
| project_brief          | 项目简介      | text         | div.house-left div.content(0) div.desc                       |
| surrounding_facilities | 周边配套      | text         | div.house-left div.content(1) div.desc                       |
| community_supporting   | 小区配套      | text         | div.house-left div.content(2) div.desc                       |
| traffic_situation      | 交通情况      | text         | div.house-left div.content(3) div.desc                       |
| floor_condition        | 楼层状况      | text         | div.house-left div.content(4) div.desc                       |
| delivery_standard      | 交付标准      | text         | div.house-left div.content(5) div.desc                       |
| other_information      | 其他信息      | text         | div.house-left div.baseInfo ul[last()]                       |
| url                    | 当前网址      | varchat(255) |                                                              |

### 数据库代码：

```mysql
CREATE TABLE `crawler`.`house_info`  (
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
  `term_of_property_right` int NULL DEFAULT NULL COMMENT '产权年限',
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

```mysql
CREATE TABLE `crawler`.`house_info`  (
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

