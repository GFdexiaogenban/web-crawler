package house.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HouseInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private int id;// 编号
  private String houseName;//房屋名称
  private String houseAlias;//房屋别名
  private String price;//价格
  private String propertyType;//物业类型
  private String propertyCompany;//物业公司
  private String decorationStatus;//装修情况
  private String openingTimePlace;//开盘时间-地点
  private String salesAddress;//售楼地址
  private String location;//项目位置
  private String projectFeatures;//项目特色
  private String checkInTime;//入住时间
  private String propertyFee;//物业费
  private String termOfPropertyRight;//产权年限
  private String preSalePermit;//预售许可证
  private String issuingTime;//发证时间
  private String buildingNumber;//楼栋号
  private String architecturalPlanning;//建筑规划
  private String projectBrief;//项目简介
  private String surroundingFacilities;//周边配套
  private String communitySupporting;//小区配套
  private String trafficSituation;//交通情况
  private String floorCondition;//楼层状况
  private String deliveryStandard;//交付标准
  private String otherInformation;//其他信息
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHouseName() {
    return houseName;
  }

  public void setHouseName(String houseName) {
    this.houseName = houseName;
  }

  public String getHouseAlias() {
    return houseAlias;
  }

  public void setHouseAlias(String houseAlise) {
    this.houseAlias = houseAlise;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getPropertyType() {
    return propertyType;
  }

  public void setPropertyType(String propertyType) {
    this.propertyType = propertyType;
  }

  public String getPropertyCompany() {
    return propertyCompany;
  }

  public void setPropertyCompany(String propertyCompany) {
    this.propertyCompany = propertyCompany;
  }

  public String getDecorationStatus() {
    return decorationStatus;
  }

  public void setDecorationStatus(String decorationStatus) {
    this.decorationStatus = decorationStatus;
  }

  public String getOpeningTimePlace() {
    return openingTimePlace;
  }

  public void setOpeningTimePlace(String openingTimePlace) {
    this.openingTimePlace = openingTimePlace;
  }

  public String getSalesAddress() {
    return salesAddress;
  }

  public void setSalesAddress(String salesAddress) {
    this.salesAddress = salesAddress;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getProjectFeatures() {
    return projectFeatures;
  }

  public void setProjectFeatures(String projectFeatures) {
    this.projectFeatures = projectFeatures;
  }

  public String getCheckInTime() {
    return checkInTime;
  }

  public void setCheckInTime(String checkInTime) {
    this.checkInTime = checkInTime;
  }

  public String getPropertyFee() {
    return propertyFee;
  }

  public void setPropertyFee(String propertyFee) {
    this.propertyFee = propertyFee;
  }

  public String getTermOfPropertyRight() {
    return termOfPropertyRight;
  }

  public void setTermOfPropertyRight(String termOfPropertyRight) {
    this.termOfPropertyRight = termOfPropertyRight;
  }

  public String getPreSalePermit() {
    return preSalePermit;
  }

  public void setPreSalePermit(String preSalePermit) {
    this.preSalePermit = preSalePermit;
  }

  public String getIssuingTime() {
    return issuingTime;
  }

  public void setIssuingTime(String issuingTime) {
    this.issuingTime = issuingTime;
  }

  public String getBuildingNumber() {
    return buildingNumber;
  }

  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  public String getArchitecturalPlanning() {
    return architecturalPlanning;
  }

  public void setArchitecturalPlanning(String architecturalPlanning) {
    this.architecturalPlanning = architecturalPlanning;
  }

  public String getProjectBrief() {
    return projectBrief;
  }

  public void setProjectBrief(String projectBrief) {
    this.projectBrief = projectBrief;
  }

  public String getSurroundingFacilities() {
    return surroundingFacilities;
  }

  public void setSurroundingFacilities(String surroundingFacilities) {
    this.surroundingFacilities = surroundingFacilities;
  }

  public String getCommunitySupporting() {
    return communitySupporting;
  }

  public void setCommunitySupporting(String communitySupporting) {
    this.communitySupporting = communitySupporting;
  }

  public String getTrafficSituation() {
    return trafficSituation;
  }

  public void setTrafficSituation(String trafficSituation) {
    this.trafficSituation = trafficSituation;
  }

  public String getFloorCondition() {
    return floorCondition;
  }

  public void setFloorCondition(String floorCondition) {
    this.floorCondition = floorCondition;
  }

  public String getDeliveryStandard() {
    return deliveryStandard;
  }

  public void setDeliveryStandard(String deliveryStandard) {
    this.deliveryStandard = deliveryStandard;
  }

  public String getOtherInformation() {
    return otherInformation;
  }

  public void setOtherInformation(String otherInformation) {
    this.otherInformation = otherInformation;
  }

  @Override
  public String toString() {
    return "HouseInfo{" +
        "id=" + id +
        ", houseName='" + houseName + '\'' +
        ", houseAlias='" + houseAlias + '\'' +
        ", price='" + price + '\'' +
        ", propertyType='" + propertyType + '\'' +
        ", propertyCompany='" + propertyCompany + '\'' +
        ", decorationStatus='" + decorationStatus + '\'' +
        ", openingTimePlace='" + openingTimePlace + '\'' +
        ", salesAddress='" + salesAddress + '\'' +
        ", location='" + location + '\'' +
        ", projectFeatures='" + projectFeatures + '\'' +
        ", checkInTime='" + checkInTime + '\'' +
        ", propertyFee='" + propertyFee + '\'' +
        ", termOfPropertyRight='" + termOfPropertyRight + '\'' +
        ", preSalePermit='" + preSalePermit + '\'' +
        ", issuingTime='" + issuingTime + '\'' +
        ", buildingNumber='" + buildingNumber + '\'' +
        ", architecturalPlanning='" + architecturalPlanning + '\'' +
        ", projectBrief='" + projectBrief + '\'' +
        ", surroundingFacilities='" + surroundingFacilities + '\'' +
        ", communitySupporting='" + communitySupporting + '\'' +
        ", trafficSituation='" + trafficSituation + '\'' +
        ", floorCondition='" + floorCondition + '\'' +
        ", deliveryStandard='" + deliveryStandard + '\'' +
        ", otherInformation='" + otherInformation + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
