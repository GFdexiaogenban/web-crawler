package house.task;

import house.pojo.HousePhoto;
import house.utils.PicDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.List;

@Component
public class HouseProcessor implements PageProcessor {

    private String url = "https://cq.loupan.com/xinfang/p1/";
    // private String url = "https://cq.loupan.com/photo/list-7096731.html";
    // private String url = "https://cq.loupan.com/photo/list-7096731.html";
    // private String url = "https://cq.loupan.com/photo/list-7067727-3.html";
    // private String url = "https://cq.loupan.com/photo/7096731/tu2-95036.html";


    @Override
    public void process(Page page) {
        //解析页面，获取招聘信息详情的URL地址
        List<Selectable> list = page.getHtml().css("ul.list-house li.item").nodes();

        System.out.println(1);
        // /html/body/div[2]/div[1]/div[6]/div[1]/div[1]/ul/li[1]
        //判断是否为空
        if (list.size() == 0) {
            // 如果为空，说明可能为楼盘信息简介页，楼盘相册列表页，楼盘相册详情页
            // 楼盘信息简介页
            String detailInfoUrl = page.getHtml().xpath("//div[@id='navigate']/a[1]").links().toString();
            // 楼盘相册列表页
            String photoListPageUrl = page.getHtml().xpath("//div[@id='navigate']/a[5]").links().toString();
            // 当前页面网址
            String pageUrl = page.getUrl().toString();


            // System.out.println(1);
            //判断当前页面是否是楼盘相册列表页
            if (pageUrl.equals(photoListPageUrl)) {
                //如果是楼盘相册列表页，则把照片种类页面加入队列中
                // List<Selectable> picList = page.getHtml().xpath("//div[@class='container box']/div[@class='house-left']/div[@class='hxlb']/ul/li").nodes();
                List<Selectable> picList = page.getHtml().xpath("//div[@class='hx-nav']/ul/li/a").nodes();
                int x = picList.size();
                for (int i = 0; i < picList.size() - 1; i++) {
                    Selectable selectable = picList.get(i);
                    String contentUrl = selectable.links().toString();
                    // 添加到队列中
                    page.addTargetRequest(contentUrl);
                }

            } else if (pageUrl.equals(detailInfoUrl)) {
                //当前页面是楼盘信息简介页，则将楼盘相册列表页加入队列中
                page.addTargetRequest(photoListPageUrl);
            } else {
                // 当前页面既不是户型图列表页，也不是楼盘信息简介页，则是照片种类页面，进行数据保存
                // 还需要将[下一页]加入队列

                // 获取下一页的url
                String nextPageUrl = page.getHtml().xpath("//div[@class='page-turn']/div/a[@class='pagenxt']").links().toString();

                if (nextPageUrl != null) {
                    // 把nextPageUrl放入任务队列中
                    page.addTargetRequest(nextPageUrl);
                }

                // String checkUrl = page.getHtml().xpath("//div[@class='slideFixed']/ul/li[@class='active]/a").links().toString();
                List<Selectable> picUrlList = page.getHtml().xpath("//div[@class='xclb']/ul/li").nodes();
                if (picUrlList.size() > 0) {
                    for (Selectable selectable : picUrlList) {
                        String finalPicUrl = selectable.links().toString();
                        // 添加到队列中
                        page.addTargetRequest(finalPicUrl);
                    }
                }else{
                    try {
                        this.saveInfo(page);//解析页面，并保存数据
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            // String jobInfoUrl = list.get(0).links().toString();
            // // 添加到队列中
            // page.addTargetRequest(jobInfoUrl);
            // //不为空，表示是列表页，解析出详情页的url地址，添加到队列中
            for (Selectable selectable : list) {
                String jobInfoUrl = selectable.links().toString();
                // 添加到队列中
                page.addTargetRequest(jobInfoUrl);
                // System.out.println(jobInfoUrl);
            }
            // 获取下一页的url
            String nextPageUrl = page.getHtml().css("div.page-turn a.pagenxt").nodes().get(0).links().toString();
            // 把nextPageUrl放入任务队列中
            page.addTargetRequest(nextPageUrl);
        }
    }

    // 解析页面, 获取招聘详情信息, 保存数据
    private void saveInfo(Page page) throws IOException {
        //解析页面，并保存数据
        // 创建招聘详情对象
        HousePhoto housePhoto = new HousePhoto();

        // 解析页面
        Html html = page.getHtml();
        String houseName = html.xpath("//div[@class='title box']/div[@class='t']/h1/text()").toString();
        String currentUrl = page.getUrl().toString();
        String picType = "";
        String picFirstName = html.xpath("//div[@class='slideFixed']/ul[@class='slides']/li[@class='active']/img/@alt").toString();


        if (currentUrl.contains("tu3-")) {
            picType = "实景图";
            System.out.println("实景图");
        } else if (currentUrl.contains("tu5-")) {
            picType = "样板间";
            System.out.println("样板间");
        } else if (currentUrl.contains("tu4-")) {
            picType = "配套图";
            System.out.println("配套图");
        } else if (currentUrl.contains("tu1-")) {
            picType = "效果图";
            System.out.println("效果图");
        } else if (currentUrl.contains("tu2-")) {
            picType = "位置图";
            System.out.println("位置图");
        }

        List<Selectable> imgUrlList = page.getHtml().xpath("//div[@class='slideFixed']/ul[@class='slides']/li").nodes();
        String imgUrl = html.xpath("//div[@class='slideFixed']/ul[@class='slides']/li[@class='active']/img/@src").toString();
        int i = 0;
        int picNum = 0;
        for (Selectable selectable : imgUrlList) {
            i++;
            String context = selectable.toString();
            if (context.contains("active")) {
                picNum = i;
                break;
            }
        }
        String picName = picFirstName.concat("_") + picNum;


        String picDirectory = "";
        PicDownload picDownload = new PicDownload();
        picDirectory = picDownload.download(imgUrl, houseName, picType, picName.concat(".jpg"));

        housePhoto.setHouseName(houseName);
        housePhoto.setPicType(picType);
        housePhoto.setPicName(picName);
        housePhoto.setPicDirectory(picDirectory);
        housePhoto.setUrl(currentUrl);

        System.out.println(11111);
        // 把结果保存起来，只是保存到内存中
        page.putField("housePhoto", housePhoto);
        System.out.println(111);
    }


    private Site site = Site.me()
            .setCharset("utf8") // 设置编码
            .setTimeOut(10 * 1000) // 设置超时时间
            .setRetrySleepTime(3000) // 设置重试的间隔时间
            .setRetryTimes(3); // 设置重试的次数

    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private SpringDataPipeline springDataPipeline;

    // initialDelay当任务启动后, 等多久执行方法
    // fixedDelay每隔多久执行方法
    @Scheduled(initialDelay = 1000, fixedDelay = 100000)
    public void process() {
        Spider.create(new HouseProcessor())
                .addUrl(url)
                // 设置Secheduler，设置Bloom去重
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(1)
                // 设置自定义的Pipeline储存数据
                .addPipeline(this.springDataPipeline)
                .run();
    }
}
