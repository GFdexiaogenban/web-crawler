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

