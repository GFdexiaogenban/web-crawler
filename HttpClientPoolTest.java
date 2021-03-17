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
