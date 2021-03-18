## 简介

核心部分是一个精简的, 模块化的爬虫实现, 而扩展部分则包括一些遍历的, 使用性的功能

设计目标是尽量的模块化, 并体现爬虫的功能特点, 这部分提供非常简单, 灵活的API, 在基本不改变开发模式的情况下, 编写一个爬虫

扩展部分提供一些便捷的功能, 例如注解模式编写爬虫等, 同时内置了一些常用的组件, 便于爬虫开发

## 架构

WebMagic的结构分为Downloader、PageProcessor、Scheduler、Pipeline四大组件，并由Spider将它们彼此组织起来。这四大组件对应爬虫生命周期中的下载、处理、管理和持久化等功能。WebMagic的设计参考了Scapy，但是实现方式更Java化一些。

而Spider则将这几个组件组织起来，让它们可以互相交互，流程化的执行，可以认为Spider是一个大的容器，它也是WebMagic逻辑的核心。

WebMagic总体架构图如下

![image-20210318105827188](D:\myNotes\img\Untitled\image-20210318105827188.png)

## WebMagic的四个组件

1. ### Downloader

   Downloader负责从互联网上下载页面，以便后续处理。WebMagic默认使用了Apache HttpClient作为下载工具。

2. ### PageProcessor

   PageProcessor负责解析页面，抽取有用信息，以及发现新的链接。WebMagic使用Jsoup作为HTML解析工具，并基于其开发了解析XPath的工具Xsoup。

   在这四个组件中，PageProcessor对于每个站点每个页面都不一样，是需要使用者定制的部分。

3. ### Scheduler

   Scheduler负责管理待抓取的URL，以及一些去重的工作。WebMagic默认提供了JDK的内存队列来管理URL，并用集合来进行去重。也支持使用Redis进行分布式管理。

4. ### Pipeline

   Pipeline负责抽取结果的处理，包括计算、持久化到文件、数据库等。WebMagic默认提供了“输出到控制台”和“保存到文件”两种结果处理方案。

   Pipeline定义了结果保存的方式，如果你要保存到指定数据库，则需要编写对应的Pipeline。对于一类需求一般只需编写一个Pipeline。

## 关于数据流转的对象

1. ### Request

   `Request`是对`URL`地址的一层`封装`，`一个Request对应一个URL地址`

   它是`PageProcessor与Downloader交互的载体`，也是PageProcessor控制Downloader唯一方式

   除了URL本身外，它还包含一个Key-Value结构的字段extra。可以在extra中保存一些特殊的属性，然后在其他地方读取，以完成不同的功能。例如附加上一个页面的一些信息等

   `Request = URL + extra`

   `extra = Key + Value`

2. ### Page

   Page代表了从Downloader下载到的一个页面——可能是HTML，也可能是JSON或者其他文本格式的内容。

   Page是WebMagic抽取过程的核心对象，它提供一些方法可供抽取、结果保存等

3. ### ResultItems

   ResultItems相当于一个`Map`，它保存PageProcessor处理的结果，供`Pipeline`使用。它的API与Map很类似，值得注意的是它一个字段skip，若设置为true，则不应被Pipeline处理

## HelloWorld

### 导入pom依赖

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/us.codecraft/webmagic-core -->
    <!--WebMagic核心包-->
    <dependency>
        <groupId>us.codecraft</groupId>
        <artifactId>webmagic-core</artifactId>
        <version>0.7.4</version>
    </dependency>

    <!--WebMagic扩展包-->
    <!-- https://mvnrepository.com/artifact/us.codecraft/webmagic-extension -->
    <dependency>
        <groupId>us.codecraft</groupId>
        <artifactId>webmagic-extension</artifactId>
        <version>0.7.4</version>
    </dependency>
    <!--解决日志报错-->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-nop</artifactId>
        <version>1.7.6</version>
    </dependency>
</dependencies>
```

在[项目工程]--[src]--[main]--[resources]中添加`log4j.properties`

```properties
log4j.rootLogger=INFO,A1

log4j.appender.A1=org.apache.log4j.ConsoleAppender
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.A1.layout.ConversionPattern=%-d{yyyy-MM-dd HH??ss,SSS} [%t] [%c]-[%p] %m%n
```

### 实例

```java
package webMagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

// 四个组件
// Downloader
// PageProcessor
// Scheduler
// Pipeline

public class JobProcessor implements PageProcessor {

    //解析页面
    public void process(Page page) {
        //page是一个html页面，或者json文本
        //key:value
        //解析返回的数据page，并且把解析的结果放到ResultItems中
        //css选择器
        page.putField("div1",page.getHtml().css("div.w li.fore2 div.dt").all());

        //XPath
        page.putField("div2",page.getHtml().xpath("//div[@id=shortcut]/div[@class=w]"));

        //正则表达式
        page.putField("div3",page.getHtml().css("div#shortcut div.cw-icon").regex(".*服务.*").all());
    }

    private Site site = Site.me();


    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        Spider.create(new JobProcessor())
                .addUrl("http://www.jd.com/moreSubject.aspx")//初始访问地址
                .run();//执行爬虫
    }
}

```

## 实现PageProcessor

### 抽取元素Selectable

WebMagic里主要使用三种抽取技术

1. XPath
2. 正则表达式
3. CSS选择器

此外，对于JSON格式的内容，可使用JsonPath进行解析

### XPath

使用方式：

1. 属性定位

   `//div[@class="song"]`

2. 层级&索引定位

   `//div[@class="tang"]/ul/li[2]/a`

3. 逻辑运算

   `//a[@href="" and @class="du"]`

4. 模糊匹配

   `//div[contains(@class, "ng")]`

5. 取文本

   `//div[@class="song"]/p[1]/text()`

6. 取属性

   `//div[@class="tang"]//li[2]/a/@href`

### 抽取API

Selectable相关的抽取元素`链式`API是WebMagic的一个核心功能

使用Selectable接口，可以直接完成页面元素的链式抽取，也无需去关心抽取的细节

`page.getHtml()`返回的是一个`Html`对象，它实现了Selectable接口。这个接口包含的方法分为两类：`抽取部分`和`获取结果部分`

| **方法**                       | **说明**                     | 示例                                |
| ------------------------------ | ---------------------------- | ----------------------------------- |
| xpath(String xpath)            | 使用XPath选择                | html.xpath("//div[@class='title']") |
| $(String selector)             | 使用Css选择器选择            | html.$("div.title")                 |
| $(String selector,String attr) | 使用Css选择器选择            | html.$("div.title","text")          |
| css(String selector)           | 功能同$()，使用Css选择器选择 | html.css("div.title")               |
| links()                        | 选择所有链接                 | html.links()                        |
| regex(String regex)            | 使用正则表达式抽取           | html.regex("(.*?)")                 |

### 获取结果

使用一条抽取规则，`XPath`、`CSS选择器`、`正则表达式`，都有可能抽取到多条元素

`WebMagic`可以通过不同的API获取到一个或者多个元素

| 方法       | 说明                                        | 示例                                |
| ---------- | ------------------------------------------- | ----------------------------------- |
| get()      | 返回一条String类型的结果（第一条）          | String link = html.links().get()    |
| toString() | 同get()，返回一条String类型的结果（第一条） | String link = html.links().toString |
| all()      | 返回所有抽取结果                            | List links = html.links().all()     |

### 获取链接

如何发现后续的链接，是一个爬虫不可缺少的一部分

`WebMagic`通过`page.addTargetRequests(url)`不断地将连接加入队列

```java
package webMagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class JobProcessor implements PageProcessor {

    //解析页面
    public void process(Page page) {
        //page是一个html页面，或者json文本
        //key:value
        page.putField("title",page.getHtml().css("title"));

        //获取链接
        page.addTargetRequests(page.getHtml().css("div.fs").links().all());

    }

    private Site site = Site.me();


    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        Spider.create(new JobProcessor())
                .addUrl("http://www.jd.com/moreSubject.aspx")//初始访问地址
                .run();//执行爬虫
    }
}

```

部分结果：

```cmd
get page: http://www.jd.com/moreSubject.aspx
title:	<title>京东(JD.COM)-正品低价、品质保障、配送及时、轻松购物！</title>
get page: http://jiadian.jd.com
title:	<title>家用电器</title>
get page: http://shouji.jd.com/
title:	<title>手机通讯频道</title>
```

### 使用Pipeline保存结果

`WebMagic`通过`Pipeline`组件来`保存结果`

“控制台输出结果”是通过`内置(默认)的Pipeline`来实现的

`内置(默认)的Pipeline`叫做`ConsolePipeline`

想要把结果用保存到文件中，只将`Pipeline`的实现换成`FilePipeline`就可以了

`Pipeline`具有多种输出方式，可通过查询父类的子类来进行查看

## Spider

Spider是爬虫启动的入口。在启动爬虫之前，需要使用一个PageProcessor创建一个Spider对象，然后使用run()进行启动

Spider的其他组件（Downloader、Scheduler、Pipeline）都可以通过set方法来进行设置

| **方法**                  | **说明**                                         | **示例**                                                     |
| ------------------------- | ------------------------------------------------ | ------------------------------------------------------------ |
| create(PageProcessor)     | 创建Spider                                       | Spider.create(new GithubRepoProcessor())                     |
| addUrl(String…)           | 添加初始的URL                                    | spider .addUrl("http://webmagic.io/docs/")                   |
| thread(n)                 | 开启n个线程                                      | spider.thread(5)                                             |
| run()                     | 启动，会阻塞当前线程执行                         | spider.run()                                                 |
| start()/runAsync()        | 异步启动，当前线程继续执行                       | spider.start()                                               |
| stop()                    | 停止爬虫                                         | spider.stop()                                                |
| addPipeline(Pipeline)     | 添加一个Pipeline，一个Spider可以有多个Pipeline   | spider .addPipeline(new ConsolePipeline())                   |
| setScheduler(Scheduler)   | 设置Scheduler，一个Spider只能有个一个Scheduler   | spider.setScheduler(new RedisScheduler())                    |
| setDownloader(Downloader) | 设置Downloader，一个Spider只能有个一个Downloader | spider .setDownloader(new SeleniumDownloader())              |
| get(String)               | 同步调用，并直接取得结果                         | ResultItems result = spider.get("http://webmagic.io/docs/")  |
| getAll(String…)           | 同步调用，并直接取得一堆结果                     | List results = spider.getAll("http://webmagic.io/docs/", "http://webmagic.io/xxx") |

## 爬虫设置Site

`Site.me()`可以对爬虫进行一些配置配置，包括编码、抓取间隔、超时时间、重试次数等

站点本身的一些配置信息，例如编码、HTTP头、超时时间、重试策略等、代理等，都可以通过设置Site对象来进行配置

| **方法**                 | **说明**                                  | **示例**                                                     |
| ------------------------ | ----------------------------------------- | ------------------------------------------------------------ |
| setCharset(String)       | 设置编码                                  | site.setCharset("utf-8")                                     |
| setUserAgent(String)     | 设置UserAgent                             | site.setUserAgent("Spider")                                  |
| setTimeOut(int)          | 设置超时时间，单位是毫秒                  | site.setTimeOut(3000)                                        |
| setRetryTimes(int)       | 设置重试次数                              | site.setRetryTimes(3)                                        |
| setCycleRetryTimes(int)  | 设置循环重试次数                          | site.setCycleRetryTimes(3)                                   |
| addCookie(String,String) | 添加一条cookie                            | site.addCookie("dotcomt_user","code4craft")                  |
| setDomain(String)        | 设置域名，需设置域名后，addCookie才可生效 | site.setDomain("github.com")                                 |
| addHeader(String,String) | 添加一条addHeader                         | site.addHeader("Referer","[https://github.com](https://github.com/)") |
| setHttpProxy(HttpHost)   | 设置Http代理                              | site.setHttpProxy(new HttpHost("127.0.0.1",8080))            |

```java
package webMagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class JobProcessor implements PageProcessor {

    //解析页面
    public void process(Page page) {
        //page是一个html页面，或者json文本
        //key:value
        //解析返回的数据page，并且把解析的结果放到ResultItems中
        page.putField("title",page.getHtml().css("title"));

        //获取链接
        page.addTargetRequests(page.getHtml().css("div.fs").links().all());

    }

    private Site site = Site.me()
            .setCharset("utf8")//设置编码
            .setTimeOut(10000)//设置超时时间
            .setRetrySleepTime(3000)//设置重试间隔时间，失败过后多久进行重试
            .setSleepTime(3)//设置重试次数
            ;


    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        Spider.create(new JobProcessor())
                .addUrl("http://www.jd.com/moreSubject.aspx")//初始访问地址
                .thread(5)//设置多线程，此处为5
                .run();//执行爬虫
    }
}

```

## 爬虫分配

1. 通用网络爬虫
2. 聚焦网络爬虫
3. 增量式网络爬虫
4. Deep Web网络爬虫

### 通用网络爬虫

通用网络爬虫又称全网爬虫（Scalable Web Crawler），爬行对象从一些种子 URL 扩充到整个 Web，主要为门户站点搜索引擎和大型 Web 服务提供商采集数据。

这类网络爬虫的爬行范围和数量巨大，对于爬行速度和存储空间要求较高，对于爬行页面的顺序要求相对较低，同时由于待刷新的页面太多，通常采用并行工作方式，但需要较长时间才能刷新一次页面。 简单的说就是互联网上抓取所有数据。

### 聚焦网络爬虫

聚焦网络爬虫（Focused Crawler），又称主题网络爬虫（Topical Crawler），是指选择性地爬行那些与预先定义好的主题相关页面的网络爬虫。

和通用网络爬虫相比，聚焦爬虫只需要爬行与主题相关的页面，极大地节省了硬件和网络资源，保存的页面也由于数量少而更新快，还可以很好地满足一些特定人群对特定领域信息的需求 。简单的说就是互联网上只抓取某一种数据。

### 增量式网络爬虫

增量式网络爬虫（Incremental Web Crawler）是 指 对 已 下 载 网 页 采 取 增量式更新和只爬行新产生的或者已经发生变化网页的爬虫，它能够在一定程度上保证所爬行的页面是尽可能新的页面。

和周期性爬行和刷新页面的网络爬虫相比，增量式爬虫只会在需要的时候爬行新产生或发生更新的页面 ，并不重新下载没有发生变化的页面，可有效减少数据下载量，及时更新已爬行的网页，减小时间和空间上的耗费，但是增加了爬行算法的复杂度和实现难度。简单的说就是互联网上只抓取刚刚更新的数据。

### Deep Web 爬虫

Web 页面按存在方式可以分为表层网页（Surface Web）和深层网页（Deep Web，也称 Invisible Web Pages 或 Hidden Web）。

表层网页是指传统搜索引擎可以索引的页面，以超链接可以到达的静态网页为主构成的 Web 页面。Deep Web 是那些大部分内容不能通过静态链接获取的、隐藏在搜索表单后的，只有用户提交一些关键词或者登陆后才能获得的 Web 页面。