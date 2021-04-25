package house.task;

import house.pojo.HouseTrendInfo;
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
    // private String url = "https://cq.loupan.com/loupan/7067727/dt_2878325.html";
    // private String url = "https://cq.loupan.com/loupan/7067727/dt_2878325.html";
    // private String url = "https://cq.loupan.com/info/7108894.html";

    @Override
    public void process(Page page) {
        //解析页面，获取招聘信息详情的URL地址
        List<Selectable> list = page.getHtml().css("ul.list-house li.item").nodes();

        System.out.println(1);
        // /html/body/div[2]/div[1]/div[6]/div[1]/div[1]/ul/li[1]
        //判断是否为空
        if (list.size() == 0) {
            // 如果为空，说明可能为楼盘信息简介页，楼盘动态列表页，楼盘动态详情页
            // 楼盘详情页
            String detailInfoUrl = page.getHtml().xpath("//div[@id='navigate']/a[1]").links().toString();
            // 楼盘动态列表页
            String trendUrl = page.getHtml().xpath("//div[@id='navigate']/a[3]").links().toString();
            // 当前页面网址
            String pageUrl = page.getUrl().toString();

            //判断当前页面是否是动态详情总页
            if (pageUrl.equals(trendUrl)) {
                //如果是动态详情总页，则把动态详情页加入队列中
                List<Selectable> trendList = page.getHtml().xpath("//*[@id='load-timer']/li").nodes();
                for (Selectable selectable : trendList) {
                    String contentUrl = selectable.links().toString();
                    // 添加到队列中
                    page.addTargetRequest(contentUrl);
                }
            } else if (pageUrl.equals(detailInfoUrl)) {
                //当前页面是楼盘信息简介页，则将楼盘动态列表页加入队列中
                page.addTargetRequest(trendUrl);
            } else {
                // 当前页面既不是动态详情总页，也不是楼盘信息简介页，则是楼盘动态详情页，进行数据保存
                try {
                    this.saveJobInfo(page);//解析页面，并保存数据
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            String jobInfoUrl = list.get(0).links().toString();
            // 添加到队列中
            page.addTargetRequest(jobInfoUrl);
            // //不为空，表示是列表页，解析出详情页的url地址，添加到队列中
            // for (Selectable selectable : list) {
            //     String jobInfoUrl = selectable.links().toString();
            //     // 添加到队列中
            //     page.addTargetRequest(jobInfoUrl);
            //     // System.out.println(jobInfoUrl);
            // }
            // // 获取下一页的url
            // String nextPageUrl = page.getHtml().css("div.page-turn a.pagenxt").nodes().get(0).links().toString();
            // // 把nextPageUrl放入任务队列中
            // page.addTargetRequest(nextPageUrl);
        }
    }

    // 解析页面, 获取招聘详情信息, 保存数据
    private void saveJobInfo(Page page) throws IOException {
        //解析页面，并保存数据
        // 创建招聘详情对象
        HouseTrendInfo houseTrendInfo = new HouseTrendInfo();

        // 解析页面
        Html html = page.getHtml();

        System.out.println(11111);
        //提取数据

        String houseName = html.xpath("//div[@class='title box']/div[@class='t']/h1/text()").toString();
        String trendTitle = html.xpath("//div[@class='house-left']/h1/text()").toString();
        String trendTime = html.xpath("//div[@class='house-left']/div[@class='other']/div[@class='date']/text()").toString();
        List<Selectable> contentList = html.xpath("//div[@class='container box']/div[@class='house-left']/div[@class='article']/p/text()").nodes();
        String contentTotle = "";
        for (Selectable selectable : contentList) {
            String content = selectable.toString();
            contentTotle = contentTotle.concat(content);
        }
        PicDownload picDownload = new PicDownload();
        String directory = "";
        List<Selectable> picList = html.xpath("//div[@class='container box']/div[@class='house-left']/div[@class='article']/p/img").nodes();
        int i = 0;
        for (Selectable selectable : picList) {
            i++;
            String picName = selectable.xpath("//img/@title").toString();
            String picUrl = selectable.xpath("//img/@src").toString();
            directory = picDownload.download(picUrl, houseName, trendTitle, i + ".png");
            System.out.println(directory + "_" + i + ".png");
        }
        String trendUrl = page.getUrl().toString();
        houseTrendInfo.setHouseName(houseName);
        houseTrendInfo.setTrendTitle(trendTitle);
        houseTrendInfo.setTrendTime(trendTime);
        houseTrendInfo.setContents(contentTotle);
        houseTrendInfo.setDirectory(directory);
        houseTrendInfo.setTrendUrl(trendUrl);



        // // 提取数据
        // String name = html.xpath("//div[@class='title box']/div[@class='t']/h1/text()").toString();
        // String alias = html.xpath("//div[@class='title box']/div[@class='t']/p/text()").toString();
        // String price = Jsoup.parse(html.css("div.house-left div.baseInfo li").nodes().get(0).toString()).text();
        // String propertyType = html.css("div.house-left div.baseInfo li span", "text").nodes().get(2).toString();
        // String propertyCompany = html.css("div.house-left div.baseInfo li span", "text").nodes().get(4).toString();
        // String decorationStatus = html.css("div.house-left div.baseInfo li span", "text").nodes().get(6).toString();
        // String openingTimePlace = html.xpath("//div[@class='wuzhengFont']/text()").toString();
        // String salesAddress = html.css("div.house-left div.baseInfo li.full", "text").nodes().get(1).toString();
        // String location = html.css("div.house-left div.baseInfo li.full", "text").nodes().get(2).toString();
        // String projectFeatures = html.css("div.house-left div.baseInfo li span", "text").nodes().get(1).toString();
        // String checkInTime = html.css("div.house-left div.baseInfo li span", "text").nodes().get(3).toString();
        // String propertyFee = html.css("div.house-left div.baseInfo li span", "text").nodes().get(5).toString();
        // String termOfPropertyRight = html.css("div.house-left div.baseInfo li span", "text").nodes().get(7).toString();
        // String perSalePermit = null;
        // String issuingTime = null;
        // String buildingNumber = null;
        // if (html.css("div.house-left div.baseInfo table.licence-table tbody td", "text").toString() != null) {
        //   perSalePermit = Jsoup.parse(html.css("div.house-left div.baseInfo table.licence-table tbody td").nodes().get(0).toString()).text();
        //   issuingTime = Jsoup.parse(html.css("div.house-left div.baseInfo table.licence-table tbody td").nodes().get(1).toString()).text();
        //   buildingNumber = Jsoup.parse(html.css("div.house-left div.baseInfo table.licence-table tbody td").nodes().get(2).toString()).text();
        // }
        // int baseInfoNumber = html.xpath("//div[@class='house-left']/div[@class='baseInfo']/h5").nodes().size();
        // String architecturalPlanning = Jsoup.parse(html.xpath("//div[@class='house-left']/div[@class='baseInfo']/ul").nodes().get(baseInfoNumber - 2).toString()).text();
        // String projectBrief = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(0).toString()).text();
        // String surroundingFacilities = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(1).toString()).text();
        // String communitySupporting = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(2).toString()).text();
        // String trafficSituation = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(3).toString()).text();
        // String floorCondition = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(4).toString()).text();
        // String deliveryStandard = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(5).toString()).text();
        // String otherInformation = Jsoup.parse(html.xpath("//div[@class='house-left']/div[@class='baseInfo']/ul").nodes().get(baseInfoNumber - 1).toString()).text();
        // String homeUrl = html.xpath("//div[@class='navigate box' and @id='navigate']/a[@class='active']").links().toString();
        // houseInfo.setHouseName(name);
        // houseInfo.setHouseAlias(alias);
        // houseInfo.setPrice(price);
        // houseInfo.setPropertyType(propertyType);
        // houseInfo.setPropertyCompany(propertyCompany);
        // houseInfo.setDecorationStatus(decorationStatus);
        // houseInfo.setOpeningTimePlace(openingTimePlace);
        // houseInfo.setSalesAddress(salesAddress);
        // houseInfo.setLocation(location);
        // houseInfo.setProjectFeatures(projectFeatures);
        // houseInfo.setCheckInTime(checkInTime);
        // houseInfo.setPropertyFee(propertyFee);
        // houseInfo.setTermOfPropertyRight(termOfPropertyRight);
        // houseInfo.setPreSalePermit(perSalePermit);
        // houseInfo.setIssuingTime(issuingTime);
        // houseInfo.setBuildingNumber(buildingNumber);
        // houseInfo.setArchitecturalPlanning(architecturalPlanning);
        // houseInfo.setProjectBrief(projectBrief);
        // houseInfo.setSurroundingFacilities(surroundingFacilities);
        // houseInfo.setCommunitySupporting(communitySupporting);
        // houseInfo.setTrafficSituation(trafficSituation);
        // houseInfo.setFloorCondition(floorCondition);
        // houseInfo.setDeliveryStandard(deliveryStandard);
        // houseInfo.setOtherInformation(otherInformation);
        // houseInfo.setUrl(homeUrl);

        System.out.println(11111);
        // 把结果保存起来，只是保存到内存中
        page.putField("trendInfo", houseTrendInfo);
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
