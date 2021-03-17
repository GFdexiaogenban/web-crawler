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
