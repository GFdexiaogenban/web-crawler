## URIBuilder

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

