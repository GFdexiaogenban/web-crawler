jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。

解析页面内容可以用字符串处理工具（比较麻烦），也可以使用正则表达式（也比较麻烦），于是需要一款专门解析HTML页面的技术——jsoup（底层）

jsoup的主要功能

1. 从一个URL，文件或字符串中解析HTML

2. 使用DOM或者CSS选择器来查找、取出数据

3. 可操作HTML元素、属性、文本

   jsoup一般用于解析爬取道德数据并存储，很少用于操作

jsoup需要的依赖

```xml
<!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.10.2</version>
</dependency>

<!-- https://mvnrepository.com/artifact/junit/junit -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>

<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.6</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.7</version>
</dependency>
```

## Jsoup解析URL

1. 解析url地址

   第一个参数是访问的url，第二个参数是访问时候的超时时间

   `Document document = Jsoup.parse(new URL("http://www.itcast.cn"),1000);`

2. 使用变迁选择器，获取title标签中的内容

   `String title = document.getElementsByTag("title").first().text();`

```java
package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.net.URL;

public class JsoupFirstTest {

    @Test
    public void testUrl()throws Exception{
        //解析url地址，第一个参数是访问的url，第二个参数是访问时候的超时时间
        Document document = Jsoup.parse(new URL("http://www.itcast.cn"),1000);

        //使用变迁选择器，获取title标签中的内容
        String title = document.getElementsByTag("title").first().text();

        //打印
        System.out.println(title);
    }
}

```

PS：虽然使用Jsoup可以替代HttpClient直接发起请求解析数据，但是往往不会这样用，因为实际的开发过程中，需要使用到==多线程==，==连接池==，==代理==等等方式，而==jsoup对这些的支持并不是很好==，
所以我们一般把`HttpClient`用作爬取数据，`jsoup`作为Html解析工具

## Jsoup解析字符串

```html
<html>
 <head> 
  <title>传智播客官网-一样的教育,不一样的品质</title> 
 </head> 
 <body>
	<div class="city">
		<h3 id="city_bj">北京中心</h3>
		<fb:img src="/2018czgw/images/slogan.jpg" class="slogan"/>
		<div class="city_in">
			<div class="city_con" style="display: none;">
				<ul>
					<li id="test" class="class_a class_b">
						<a href="http://www.itcast.cn" target="_blank">
							<span class="s_name">北京</span>
						</a>
					</li>
					<li>
						<a href="http://sh.itcast.cn" target="_blank">
							<span class="s_name">上海</span>
						</a>
					</li>
					<li>
						<a href="http://gz.itcast.cn" target="_blank">
							<span abc="123" class="s_name">广州</span>
						</a>
					</li>
					<ul>
						<li>天津</li>
					</ul>					
				</ul>
			</div>
		</div>
	</div>
 </body>
</html>
```

1. 通过工具类读取文件，获取字符串

   `String content = FileUtils.readFileToString(new File("C:\\Users\\ckj\\Desktop\\test.html"),"utf8");`

2. 解析字符串

   `Document doc = Jsoup.parse(content);`

   `String title = doc.getElementsByTag("title").first().text();`

```java
package jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;

public class JsoupFirstTest {
    @Test
    public void testString()throws Exception{
        //通过工具类读取文件，获取字符串
        String content = FileUtils.readFileToString(new File("C:\\Users\\ckj\\Desktop\\test.html"),"utf8");
        //解析字符串
        Document doc = Jsoup.parse(content);
        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);
    }
}

```

## Jsoup解析文件

直接进行解析文件

`Document doc = Jsoup.parse(new File("C:\\Users\\ckj\\Desktop\\test.html"),"utf8");`

```java
package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;


public class JsoupFirstTest {

    @Test
    public void testFile()throws Exception{
        //解析文件
        Document doc = Jsoup.parse(new File("C:\\Users\\ckj\\Desktop\\test.html"),"utf8");

        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);
    }
}

```

## 使用Dom方式遍历文档

元素获取

1. 根据id查询元素`getElementById`
2. 根据标签获取元素`getElementsByTag`
3. 根据`class`获取元素`getElementsByClass`
4. 根据属性获取元素`getElementsByAttribute`

```java
package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;

public class JsoupFirstTest {
    @Test
    public void testDom() throws Exception {
        // 解析文件件, 获取Document对象
        Document doc = Jsoup.parse(new File("C:\\Users\\ckj\\Desktop\\test.html"), "utf8");
        // 1.根据id查询元素getElementById
        Element element = doc.getElementById("city_bj");
        // 2.根据标签获取元素getElementsByTag
        Elements spans = doc.getElementsByTag("span");
        // 3.根据class获取元素getElementsByClass
        Element a = doc.getElementsByClass("class_a class_b").first();
        // 4.根据属性获取元素getElementsByAttribute
        Element abc = doc.getElementsByAttribute("abc").first();
        // 5.根据属性与属性值筛选
        Element href = doc.getElementsByAttributeValue("href", "http://www.itcast.cn").first();

        // 打印元素内容
        System.out.println("获取到的元素内容是: " + element.text());
        for (Element i : spans) {
            System.out.println(i.text());
        }
        System.out.println(a.text());
        System.out.println("abc.text() = " + abc.text());
        System.out.println("href.text() = " + href.text());
    }
}
```

## 从元素中获取标签

1. 从元素中获取`id`

   `element.id("id名")`

2. 从元素中获取`className`

   `element.className("class名")`

3. 从元素中获取属性的值`attr`

   `element.attr("属性名")`

4. 从元素中获取所有属性`attributes`

   `element.attributes()`

5. 从元素中获取文本内容`text`

   `element.text()`

## Selector选择器

Selector选择器可以组合灵活使用

1. el#id: 元素+ID，比如： h3#city_bj
2. el.class: 元素+class，比如： li.class_a
3. el[attr]: 元素+属性名，比如： span[abc]
4. 任意组合: 比如：span[abc].s_name

```java
package jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Set;

public class JsoupFirstTest {
    @Test
    public void testSelector2() throws Exception {
        // 解析html文件, 获取Document对象
        Document doc = Jsoup.parse(new File("C:\\Users\\ckj\\Desktop\\test.html"), "utf8");

        //el#id: 元素+ID，比如： h3#city_bj
        //Element element = doc.select("h3#city_bj").first();

        //el.class: 元素+class，比如： li.class_a
        //Element element = doc.select("li.class_a").first();

        //el[attr]: 元素+属性名，比如： span[abc]
        //Element element = doc.select("span[abc]").first();

        //任意组合: 比如：span[abc].s_name
        Element element = doc.select("span[abc].s_name").first();

        //ancestor child: 查找某个元素下子元素，比如：.city_con li 查找"city_con"下的所有li
        //Elements elements = doc.select(".city_con li");

        //parent > child: 查找某个父元素下的直接子元素，比如：
        // .city_con > ul > li 查找city_con第一级（直接子元素）的ul，再找所有ul下的第一级li
        //Elements elements = doc.select(".city_con > ul > li");

        //parent > *: 查找某个父元素下所有直接子元素
        Elements elements = doc.select(".city_con > ul > *");

        // 打印
        System.out.println("获取到的内容是: " + element.text());
        for (Element ele : elements) {
            System.out.println("遍历的结果: " + ele.text());
        }
    }
}

```

