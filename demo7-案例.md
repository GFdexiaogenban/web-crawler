1. 分析区分

   SPU、SKU

2. 

## 抓取部分京东手机信息案例

### 环境搭建

1. 创建数据库表

   ```mysql
   // 创建crawler数据库, 再创建表
   CREATE TABLE `jd_item` (
     `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
     `spu` bigint(15) DEFAULT NULL COMMENT '商品集合id',
     `sku` bigint(15) DEFAULT NULL COMMENT '商品最小品类单元id',
     `title` varchar(100) DEFAULT NULL COMMENT '商品标题',
     `price` bigint(10) DEFAULT NULL COMMENT '商品价格',
     `pic` varchar(200) DEFAULT NULL COMMENT '商品图片',
     `url` varchar(200) DEFAULT NULL COMMENT '商品详情地址',
     `created` datetime DEFAULT NULL COMMENT '创建时间',
     `updated` datetime DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`),
     KEY `sku` (`sku`) USING BTREE
   ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='京东商品表';
   ```

2. `pom.xml`依赖导入

   `Spring Boot` + `Spring Data JPA` 和定时任务

   - 继承`spring-boot`
   - 导入`SpringMVC`
   - 导入`SpringData Jpa`
   - 导入`MySQL连接包`
   - 导入`HttpClient`
   - 导入`Jsoup`
   - 导入字符串处理工具包`commons-lang3`

   ```xml
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.0.2.RELEASE</version>
   </parent>
   
   <properties>
       <maven.compiler.source>8</maven.compiler.source>
       <maven.compiler.target>8</maven.compiler.target>
   </properties>
   
   <dependencies>
       <!--SpringMVC-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
   
       <!--SpringData Jpa-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-jpa</artifactId>
       </dependency>
   
       <!--MySQL连接包-->
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
           <version>8.0.11</version>
       </dependency>
   
       <!-- HttpClient -->
       <dependency>
           <groupId>org.apache.httpcomponents</groupId>
           <artifactId>httpclient</artifactId>
       </dependency>
   
       <!--Jsoup-->
       <dependency>
           <groupId>org.jsoup</groupId>
           <artifactId>jsoup</artifactId>
           <version>1.10.3</version>
       </dependency>
   
       <!--工具包-->
       <dependency>
           <groupId>org.apache.commons</groupId>
           <artifactId>commons-lang3</artifactId>
       </dependency>
   </dependencies>
   ```

3. 配置`application.properties`文件

   ```properties
   #DB Configuration:
   spring.datasource.driver-class-name=com.mysql.jdbc.Driver
   spring.datasource.url=jdbc:mysql://127.0.0.1:3306/crawler
   spring.datasource.username=root
   spring.datasource.password=ckj
   
   #JPA Configuration:
   spring.jpa.database=MySQL
   spring.jpa.show-sql=true
   ```

### 创建`pojo`实体类

```java
package jd.pojp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "jd_item")
public class Item {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //标准产品单位（商品集合）
    private Long spu;
    //库存量单位（最小品类单元）
    private Long sku;
    //商品标题
    private String title;
    //商品价格
    private Double price;
    //商品图片
    private String pic;
    //商品详情地址
    private String url;
    //创建时间
    private Date created;
    //更新时间
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpu() {
        return spu;
    }

    public void setSpu(Long spu) {
        this.spu = spu;
    }

    public Long getSku() {
        return sku;
    }

    public void setSku(Long sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
```

#### 添加`idea`的`mysql`连接

点击 `[View]-[Tool Windows]-[Database]`

![image-20210317151013966](D:\myNotes\img\Untitled\image-20210317151013966.png)

点击 `+` ，选择 `[Data Source]-[MySQL]`

输入相应数据

![image-20210317151216497](D:\myNotes\img\Untitled\image-20210317151216497.png)

### 创建ItemDao(Interface)

创建`Dao`包，创建`ItemDao`

```java
package jd.Dao;

import jd.pojp.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItenDao extends JpaRepository<Item,Long> {

}

```

建立`service`包，建立`ItemService`接口

```java
package jd.service;

import jd.pojp.Item;

import java.util.List;

public interface ItemService {
    //保存商品
    public void save(Item item);

    //查询方法
    public List<Item> findAll(Item item);
}

```

建立`service.impl`包，创建`ItemServiceImpl`接口

```java
package jd.service.impl;


import jd.Dao.ItenDao;
import jd.pojp.Item;
import jd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItenDao itemDao;

    @Override
    public void save(Item item) {
        this.itemDao.save(item);
    }

    @Override
    public List<Item> findAll(Item item) {
        //声明查询条件
        Example<Item> example = Example.of(item);
        // 根据查询条件进行查询数据
        List<Item> list = this.itemDao.findAll(example);
        return list;
    }
}

```

### 创建引导类

```java
package jd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//使用定时任务，需要先开启定时任务，需要添加注解
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

```

## 创建HttpUtil

```java
package jd.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        this.cm.setMaxTotal(100);

        //设置主机最大连接数
        this.cm.setDefaultMaxPerRoute(10);

    }

    //根据请求地址下载页面数据
    public String doGetHtml(String url) {
        //获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //创建HttpGet请求对象，设置url
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;
        //使用HttpClient发起请求，获取相应
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                //判断Entity是否不飞空，如果不为空就能使用EntityUtiles
                if (response.getEntity() != null) {
                    String content = EntityUtils.toString(response.getEntity(), "utf8");
                    return content;
                }
                //System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }



    //下载图片
    public String doGetImage(String url) {
//获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //创建HttpGet请求对象，设置url
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;
        //使用HttpClient发起请求，获取相应
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                //判断Entity是否不为空
                if (response.getEntity() != null) {
                    //下载图片
                    //获取图片后缀
                    String extName = url.substring(url.lastIndexOf("."));

                    //创建图片名（重命名图片）
                    String picName = UUID.randomUUID().toString()+extName;

                    //下载图片
                    //声明outPutStream
                    OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\ckj\\Desktop\\iamge" + picName));
                    response.getEntity().writeTo(outputStream);

                    //返回图片的名称
                    return picName;
                }
                //System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)    //创建链接的最长时间
                .setConnectionRequestTimeout(500)   //获取链接的最长时间
                .setSocketTimeout(10000)    //数据传输的最长时间
                .build();
        return config;
    }
}

```

