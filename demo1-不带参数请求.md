## HttpGet

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

