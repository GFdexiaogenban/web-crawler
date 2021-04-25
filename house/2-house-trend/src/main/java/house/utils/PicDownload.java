package house.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PicDownload {
    public String download(String urlString, String houseName, String trendTitle, String picName) throws IOException {
        // 如果目标文件夹不存在，则创建文件夹
        // 如果目标文件夹存在，则继续
        String dirPreName = "D:\\pic/" + "HouseTrend";
        File dirPre = new File(dirPreName);
        dirPre.mkdirs();


        String dirFirstName = dirPreName.concat("/") + houseName;
        File dirFirst = new File(dirFirstName);
        dirFirst.mkdirs();

        String dirFinalName = dirFirstName.concat("/").concat(trendTitle);
        File dirLast = new File(dirFinalName);
        dirLast.mkdirs();

        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection connection = url.openConnection();
        // 输入流
        InputStream is = connection.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        String filename = dirFinalName + "/" + picName;  //下载路径及下载图片名称
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file, true);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return dirFinalName;
    }
}
