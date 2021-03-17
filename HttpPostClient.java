package crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpPostClient {
    public static void main(String[] args) {
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpPost对象，设置url
        HttpPost httpPost = new HttpPost("http://www.itcast.cn");
        System.out.println("发起请求的信息：" + httpPost);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response

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
