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
