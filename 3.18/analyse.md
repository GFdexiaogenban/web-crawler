初始网页：https://www.loupan.com/?all

城市列表，搜寻途径

div.city_con_list 

具体城市网址：

https://城市拼音首字母.loupan.com

1. 新房：

   [div.houseTag] [div.item newhouse] [a]-href

   网址：城市网址+"/"+"xinfang"+“p”+对应网页页数

   主要信息：

   ​	路径url：[pages] [pages pages-list] [div.main box] [div.left] [ul.list-house] [li] —— 后续用`*`代表

   1. 区分编号long：`*`-data-hid

   2. 图片url：`*`-data-imgUrl

   3. 详情链接url：`*` [a]-href

   4. 名称string：`*` [div.text] [h2] [a].first()

   5. 地理位置string：`*` [div.text] [div.address] [span]

   6. 地图url：`*` [div.text] [div.address] [a.map]-href

   7. 所卖户型string：`*` [li] [div.text] [div.info] [span.tag] [a]

   8. 所卖户型对应url：`*` [li] [div.text] [div.info] [span.tag] [a]-href

   9. 面积string：`*` [div.text] [div.info] [span.area] [a]

   10. 所卖全部户型url：`*` [div.text] [div.info] [span.area] [a]-href

   11. 房屋情况list：`*` [div.text] [div.label] [span]

   12. 房屋最新公告string：`*` [div.text] [div.desc desc-dt] [a]

   13. 房屋公告url：`*` [div.text] [div.desc desc-dt] [a]-href

   14. 房屋价格情况string：`*` [div.other] [div.price]

       房屋价格long：`*` [div.other] [div.price] [span]

       房屋价格string：`*` [div.other] [div.im-item im-data]-im-data-price

   15. 最大网页页码string：[pages] [pages pages-list] [div.main box] [div.left] [div.page-turn] [div.item-turn] [a].fifth()

2. 二手房

   基础网址："https://" + 城市拼音首字母 + ".esf.loupan.com"

   实际网址：基础网址 + "p" + 对应网页页数

   主要信息：

   ​	路径：[div.pages pages-list] [div.m_box] [div.main] [div.left] [div.list] [ul] [li] —— 后续用`*`表示

   1. 名称(title)string：`*` [div.text] [h2] [a]

   2. 房屋对应网页url：`*` [a]-href

   3. 图片url：`*` [a] [img]-src

   4. 地址string：`*` [div.text] [div.txt] [div.d] [p] + " " + `*` [div.text] [div.txt] [b]

   5. 房屋描述string<List>：`*` [div.text] [div.txt] [span]

   6. 地理优势string<List>：`*` [div.text] [div.txt] [div.tags] [span]

   7. 均价string（还需要进行拆解）：`*` [div.text] [div.price] [p]

      ` 157<i>万</i>`

   8. 平米价string（要去空格）：`*` [div.text] [div.price] [span]

      `12362                                        元/㎡`

   9. 

3. 楼讯

   网址：城市网址+"/"+"news"+“index-”+ 对应网页页数 + ".html"

   主要内容：

   ​	地址：[div.pages] [div.main] [div.content_box] [div.left] [ul] [li] —— `*` 

   1. 图片string：`*` [a.img] [img]-src

   2. 标题string：`*` [div.flex] [div.tit] [a]

   3. 地址url：`*` [div.flex] [div.tit] [a]-href

   4. 描述String：`*` [div.flex] [p.info]

   5. 时间String（还需要进行处理）：`*` [div.flex] [p.time]

      `<span class="f-fl">重庆楼盘网 <a href="https://cq.loupan.com/news/list-101.html" target="_blank"><i class="tb-icon"></i>其他楼讯 </a></span> 2021-03-18 14:34:45 </p>`

   6. 信息提供商string（还需要再处理）：`*` [div.flex] [p.time] [span.f-fl] 

