# java网络爬虫入门

## 网络爬虫简介

网络爬虫也叫网络机器人, 是一种可以按照一定规则自动采集互联网信息的程序或脚本, 爬虫一般分为数据采集, 处理, 储存三个部分, 从若干初始网页的URL开始抓取网页, 不断获取页面上的URL放入队列直到满足系统的一定条件停止

### 为什么要学习爬虫

1. 可以实现私人的搜索引擎
2. 大数据时代获取数据源, 作数据分析
3. 可以更好地进行搜索引擎优化 (SEO)
4. 有利于就业, 爬虫工程师需求量大, 发展空间广

## 环境准备

1. `JDK1.8`
2. `IntelliJ IDEA`
3. `Maven`

### hello world前期准备

1. 创建快速MAVEN项目

2. 导入 `pom.xml`

   ```xml
   <dependencies>
       <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12 -->
       <dependency>
           <groupId>org.slf4j</groupId>
           <artifactId>slf4j-log4j12</artifactId>
           <version>1.7.25</version>
           <!--            <scope>test</scope>-->
       </dependency>
       <!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
       <dependency>
           <groupId>org.apache.httpcomponents</groupId>
           <artifactId>httpclient</artifactId>
           <version>4.5.2</version>
       </dependency>
   </dependencies>
   ```

3. 创建`slf4j`日志配置文件

   在`resources`目录下创建 `log4j.properties` 文件，添加以下配置

   ```properties
   log4j.rootLogger=DEBUG,A1
   log4j.logger.cn.itbuild=INFO
   
   log4j.appender.A1=org.apache.log4j.ConsoleAppender
   log4j.appender.A1.layout=org.apache.log4j.PatternLayout
   log4j.appender.A1.layout.ConversionPattern=%-d{yyyy-MM-dd HH??ss,SSS} [%t] [%c]-[%p] %m%n
   ```

### hello world

```java
package crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CrawlerFirst {
    public static void main(String[] args) throws Exception {
        //1.打开浏览器
        //1.1创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.输入网址
        //2.1发起get请求，创建httpget对象
        HttpGet httpGet = new HttpGet("https://blog.csdn.net/WangHH51322/article/details/106422835");

        //3.按回车，发起请求，返回响应
        //3.1使用httpclient对象发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4.解析响应，获取数据
        //通过状态码，判断是否正常返回，即是否是200
        if(response.getStatusLine().getStatusCode() == 200){
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity,"utf8");

            System.out.println(content);
        }
    }
}

```

## HttpClient

### HttpGet不带参数

直接发起get请求，请求网页内容，不包含参数

`HttpGet httpGet = new HttpGet("http://www.itcast.cn");`

实例

```java
package crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClient {
    public static void main(String[] args) {
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpGet对象，设置url
        HttpGet httpGet = new HttpGet("http://www.itcast.cn");
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

打印结果：

```cmd
发起请求的信息：GET http://www.itcast.cn HTTP/1.1
242148
```

### HttpGet带参数

#### URIBuilder

1. 创建`URIBuilder`

   `URIBuilder uriBuilder = new URIBuilder("http://yun.itheima.com/search?");`

2. 设置参数

   `uriBuilder.setParameter("key","Java");`

3. 发送`get`请求

   `HttpGet httpGet = new HttpGet(uriBuilder.build());`

实例：

```java
package crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpGetParamTest {
    public static void main(String[] args) throws Exception {
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //请求地址：http://yun.itheima.com/search
        //参数：keys=Java
        //创建URIBuilder
        URIBuilder uriBuilder = new URIBuilder("http://yun.itheima.com/search?");
        //设置参数
        uriBuilder.setParameter("key","Java");
        //创建HttpGet对象，发送get请求
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        System.out.println("发起请求的信息：" + httpGet);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

打印结果：

```cmd
发起请求的信息：GET http://yun.itheima.com/search?key=Java HTTP/1.1
1072957
```

### POST请求

POST请求，url中没有参数，参数放到表单中进行提交

```java
package crawler;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpPostParamClient {
    public static void main(String[] args) throws Exception {
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpPost对象，设置url
        HttpPost httpPost = new HttpPost("http://www.itcast.cn/search");

        //声明List集合，封装表单中的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("keys","Java"));

        //创建表单的Entity对象
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"utf8");

        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(formEntity);

        System.out.println("发起请求的信息：" + httpPost);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            System.out.println("success");
            response = httpClient.execute(httpPost);
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

## 连接池

如果每次请求都要创建 `HttpClient` ，会有频繁创建和销毁的问题，可以使用连接池来解决这个问题。

1. 创建连接池管理器

   `PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();`

2. 设置最大连接数

   `cm.setMaxTotal(100);`

3. 设置每个主机最大连接数

   `cm.setDefaultMaxPerRoute(10);`

4. 使用连接池中的连接

   在使用的时候不能关闭连接，连接应该由连接池统一管理

```java
package crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientPoolTest {
    public static void main(String[] args){
        //创建连接池管理器
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

        //设置连接数,设置最大连接数
        cm.setMaxTotal(100);

        //设置每个主机最大连接数
        //分配链接
        cm.setDefaultMaxPerRoute(10);

        //使用连接池管理器发起请求
        doGet(cm);
        doGet(cm);
    }

    private static void doGet(PoolingHttpClientConnectionManager cm) {
        //不是每次创建新的httpclient，而是从连接池
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        HttpGet httpGet = new HttpGet("http://www.itcast.cn");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(),"utf8");

                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //这里不能关闭httpclient
            }
        }
    }
}

```

## 请求参数配置

有时候因为网络，或者目标服务器的原因，请求需要更长时间才能完成，我们需要自定义相关时间

1. 创建配置请求信息

   ```java
   RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(1000)
       .setConnectionRequestTimeout(500)
       .setSocketTimeout(10*1000)
       .build();
   ```

2. 讲请求信息交给请求

   `httpGet.setConfig(config);`

```java
package crawler;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpConfigTest {
    public static void main(String[] args) {
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpGet对象，设置url
        HttpGet httpGet = new HttpGet("http://www.itcast.cn");
        //配置请求信息
        //1.创建链接的最长时间，单位是毫秒
        //2.设置获取连接的最长时间，单位是毫秒
        //3.设置数据传输的最长时间，单位是毫秒
        //可以设置代理等，根据自己的实际情况进行调整
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(1000)
                .setConnectionRequestTimeout(500)
                .setSocketTimeout(10*1000)
                .build();
        //给请求设置请求信息
        httpGet.setConfig(config);
        
        System.out.println("发起请求的信息：" + httpGet);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

## Jsoup