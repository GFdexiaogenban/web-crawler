package house.task;

import house.pojo.HouseDetail;
import org.jsoup.Jsoup;
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

import java.util.List;

@Component
public class HouseProcessor implements PageProcessor {

  // private String url = "https://cq.loupan.com/xinfang/p1/";
  private String url = "https://cq.loupan.com/info/7020085.html";
  // private String url = "https://cq.loupan.com/info/7108894.html";

  @Override
  public void process(Page page) {
    //解析页面，获取招聘信息详情的URL地址
    List<Selectable> list = page.getHtml().css("ul.list-house li.item").nodes();
    //System.out.println(1);

    System.out.println(1);

    //判断是否为空
    if (list.size() == 0) {
      if (page.getHtml().css("div.imgText div.more").links().toString() == null) {
        // 如果为空，表示楼盘信息详情页，解析网页并保存数据
        this.saveJobInfo(page);//解析页面，并保存数据
      } else {
        //如果list.size() == 0，且获取不到详情信息页URL，表示是楼盘信息简介页，提取楼盘信息详情网页
        //String b = page.getHtml().xpath("div[@class=\"title box\"]/div[@class=\"t\"]/h1/text()").toString();
        String morePageUrl = page.getHtml().css("div.imgText div.more").links().toString();
        page.addTargetRequest(morePageUrl);
      }
    } else {
      //不为空，表示是列表页，解析出详情页的url地址，添加到队列中
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
  private void saveJobInfo(Page page) {
    //解析页面，并保存数据
    // 创建招聘详情对象
    HouseDetail houseDetail = new HouseDetail();

    // 解析页面
    Html html = page.getHtml();

    System.out.println(11111);


    // 提取数据
    String name = html.xpath("//div[@class='title box']/div[@class='t']/h1/text()").toString();
    String alias = html.xpath("//div[@class='title box']/div[@class='t']/p/text()").toString();
    String price = Jsoup.parse(html.css("div.house-left div.baseInfo li").nodes().get(0).toString()).text();
    String propertyType = html.css("div.house-left div.baseInfo li span", "text").nodes().get(2).toString();
    String propertyCompany = html.css("div.house-left div.baseInfo li span", "text").nodes().get(4).toString();
    String decorationStatus = html.css("div.house-left div.baseInfo li span", "text").nodes().get(6).toString();
    String openingTimePlace = html.xpath("//div[@class='wuzhengFont']/text()").toString();
    String salesAddress = html.css("div.house-left div.baseInfo li.full", "text").nodes().get(1).toString();
    String location = html.css("div.house-left div.baseInfo li.full", "text").nodes().get(2).toString();
    String projectFeatures = html.css("div.house-left div.baseInfo li span", "text").nodes().get(1).toString();
    String checkInTime = html.css("div.house-left div.baseInfo li span", "text").nodes().get(3).toString();
    String propertyFee = html.css("div.house-left div.baseInfo li span", "text").nodes().get(5).toString();
    String termOfPropertyRight = html.css("div.house-left div.baseInfo li span", "text").nodes().get(7).toString();
    String perSalePermit = null;
    String issuingTime = null;
    String buildingNumber = null;
    if (html.css("div.house-left div.baseInfo table.licence-table tbody td", "text").toString() != null) {
      perSalePermit = Jsoup.parse(html.css("div.house-left div.baseInfo table.licence-table tbody td").nodes().get(0).toString()).text();
      issuingTime = Jsoup.parse(html.css("div.house-left div.baseInfo table.licence-table tbody td").nodes().get(1).toString()).text();
      buildingNumber = Jsoup.parse(html.css("div.house-left div.baseInfo table.licence-table tbody td").nodes().get(2).toString()).text();
    }
    int baseInfoNumber = html.xpath("//div[@class='house-left']/div[@class='baseInfo']/h5").nodes().size();
    String architecturalPlanning = Jsoup.parse(html.xpath("//div[@class='house-left']/div[@class='baseInfo']/ul").nodes().get(baseInfoNumber - 2).toString()).text();
    String projectBrief = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(0).toString()).text();
    String surroundingFacilities = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(1).toString()).text();
    String communitySupporting = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(2).toString()).text();
    String trafficSituation = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(3).toString()).text();
    String floorCondition = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(4).toString()).text();
    String deliveryStandard = Jsoup.parse(html.css("div.house-left div.content div.desc").nodes().get(5).toString()).text();
    String otherInformation = Jsoup.parse(html.xpath("//div[@class='house-left']/div[@class='baseInfo']/ul").nodes().get(baseInfoNumber - 1).toString()).text();
    String homeUrl = html.xpath("//div[@class='navigate box' and @id='navigate']/a[@class='active']").links().toString();
    houseDetail.setHouseName(name);
    houseDetail.setHouseAlias(alias);
    houseDetail.setPrice(price);
    houseDetail.setPropertyType(propertyType);
    houseDetail.setPropertyCompany(propertyCompany);
    houseDetail.setDecorationStatus(decorationStatus);
    houseDetail.setOpeningTimePlace(openingTimePlace);
    houseDetail.setSalesAddress(salesAddress);
    houseDetail.setLocation(location);
    houseDetail.setProjectFeatures(projectFeatures);
    houseDetail.setCheckInTime(checkInTime);
    houseDetail.setPropertyFee(propertyFee);
    houseDetail.setTermOfPropertyRight(termOfPropertyRight);
    houseDetail.setPreSalePermit(perSalePermit);
    houseDetail.setIssuingTime(issuingTime);
    houseDetail.setBuildingNumber(buildingNumber);
    houseDetail.setArchitecturalPlanning(architecturalPlanning);
    houseDetail.setProjectBrief(projectBrief);
    houseDetail.setSurroundingFacilities(surroundingFacilities);
    houseDetail.setCommunitySupporting(communitySupporting);
    houseDetail.setTrafficSituation(trafficSituation);
    houseDetail.setFloorCondition(floorCondition);
    houseDetail.setDeliveryStandard(deliveryStandard);
    houseDetail.setOtherInformation(otherInformation);
    houseDetail.setUrl(homeUrl);
    // 把结果保存起来，只是保存到内存中
    page.putField("houseInfo", houseDetail);
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
