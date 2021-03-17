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
