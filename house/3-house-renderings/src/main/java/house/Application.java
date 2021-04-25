package house;

import house.utils.PicDownload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling // 开启定时任务
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        // PicDownload picDownload = new PicDownload();
        // String imgUrl = "https://static.loupan.com/newsimg/image/2020/1224/1724286177895.png";
        // String dirName = picDownload.download(imgUrl,"北辰悦来壹号","北辰悦来壹号12月工程进度播报",1);
        // System.out.println(dirName);
    }
}


