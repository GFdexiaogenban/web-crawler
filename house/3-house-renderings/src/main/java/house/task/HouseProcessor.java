package house.task;

import house.pojo.HouseTypePic;
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
    // private String url = "https://cq.loupan.com/layout/list-7067727.html";
    // private String url = "https://cq.loupan.com/loupan/7067727.html";
    // private String url = "https://cq.loupan.com/layout/7067727/tu-1853942.html";
    // private String url = "https://cq.loupan.com/layout/list-7067727-p2.html";

    @Override
    public void process(Page page) {
        //解析页面，获取招聘信息详情的URL地址
        List<Selectable> list = page.getHtml().css("ul.list-house li.item").nodes();

        System.out.println(1);
        // /html/body/div[2]/div[1]/div[6]/div[1]/div[1]/ul/li[1]
        //判断是否为空
        if (list.size() == 0) {
            // 如果为空，说明可能为楼盘信息简介页，户型图列表页，户型图详情页
            // 楼盘信息简介页
            String detailInfoUrl = page.getHtml().xpath("//div[@id='navigate']/a[1]").links().toString();
            // 户型图列表页
            String trendUrl = page.getHtml().xpath("//div[@id='navigate']/a[4]").links().toString();
            // 当前页面网址
            String pageUrl = page.getUrl().toString();

            List<Selectable> picList = page.getHtml().xpath("//div[@class='container box']/div[@class='house-left']/div[@class='hxlb']/ul/li").nodes();
            // System.out.println(1);
            //判断当前页面是否是户型图列表页
            if (picList.size()!=0) {
                //如果是户型图列表页，则把户型图详情页加入队列中
                // List<Selectable> picList = page.getHtml().xpath("//div[@class='container box']/div[@class='house-left']/div[@class='hxlb']/ul/li").nodes();
                for (Selectable selectable : picList) {
                    String contentUrl = selectable.links().toString();
                    // 添加到队列中
                    page.addTargetRequest(contentUrl);
                }
                // 获取下一页的url
                String nextPageUrl = page.getHtml().xpath("//div[@class='container box']/div[@class='house-left']/div[@class='hxlb']/div[@class='page-turn']/div[@class='item-turn']/a[@class='pagenxt']/@href").toString() ;

                if(nextPageUrl!=null){
                    // 把nextPageUrl放入任务队列中
                    page.addTargetRequest(nextPageUrl);
                }
            } else if (pageUrl.equals(detailInfoUrl)) {
                //当前页面是楼盘信息简介页，则将户型图列表页加入队列中
                page.addTargetRequest(trendUrl);
            } else {
                // 当前页面既不是户型图列表页，也不是楼盘信息简介页，则是户型图详情页，进行数据保存
                try {
                    this.saveJobInfo(page);//解析页面，并保存数据
                } catch (IOException e) {
                    e.printStackTrace();
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
    private void saveJobInfo(Page page) throws IOException {
        //解析页面，并保存数据
        // 创建招聘详情对象
        HouseTypePic houseTypePic = new HouseTypePic();

        // 解析页面
        Html html = page.getHtml();
        String houseName = html.xpath("//div[@class='title box']/div[@class='t']/h1/text()").toString();
        String houseType = html.xpath("//div[@class='hx-info']/div[@class='tit']/p/text()").toString();
        String typeName = html.xpath("//div[@class='hx-info']/div[@class='line']/span/text()").nodes().get(0).toString();
        String price = html.xpath("//div[@class='hx-info']/div[@class='line']/span/text()").nodes().get(1).toString();
        String buildingArea = html.xpath("//div[@class='hx-info']/div[@class='line']/span/text()").nodes().get(2).toString();
        String houseArea = html.xpath("//div[@class='hx-info']/div[@class='line']/span/text()").nodes().get(3).toString();
        String currentUrl = page.getUrl().toString();
        PicDownload picDownload = new PicDownload();
        String directory = "";
        List<Selectable> picList = html.xpath("//div[@class='imgText box']/ul[@class='photoTab']/li[@class='active']/div[@class='swiper photos']/div[@class='flexslider small']/div[@class='slideFixed']/ul/li/img").nodes();
        int picNum = 0;
        for (Selectable selectable : picList) {
            picNum++;
            String picUrl = selectable.xpath("//img/@src").toString();
            directory = picDownload.download(picUrl, houseName, houseType, typeName, picNum + ".png");
            System.out.println(directory + "_" + picNum + ".png");
        }
        houseTypePic.setHouseName(houseName);
        houseTypePic.setHouseType(houseType);
        houseTypePic.setTypeName(typeName);
        houseTypePic.setPrice(price);
        houseTypePic.setBuildingArea(buildingArea);
        houseTypePic.setHouseArea(houseArea);
        houseTypePic.setPicDirectory(directory);
        houseTypePic.setPicNum(picNum);
        houseTypePic.setUrl(currentUrl);

        System.out.println(11111);
        // 把结果保存起来，只是保存到内存中
        page.putField("houseTypePic", houseTypePic);
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
