package house.service;

import house.pojo.HouseDetail;

import java.util.List;


public interface HouseInfoService {

    /**
     * 保存房屋信息
     * @param houseDetail
     */
    public void save(HouseDetail houseDetail);

    /**
     * 根据条件查询房屋信息
     * @param houseDetail
     * @return
     */
    public List<HouseDetail> findHouseInfo(HouseDetail houseDetail);
}