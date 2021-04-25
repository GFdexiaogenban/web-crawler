package house.service;

import house.pojo.HouseTypePic;

import java.util.List;


public interface HouseInfoService {

    /**
     * 保存房屋信息
     * @param houseTypePic
     */
    public void save(HouseTypePic houseTypePic);

    /**
     * 根据条件查询房屋信息
     * @param houseTypePic
     * @return
     */
    public List<HouseTypePic> findHouseInfo(HouseTypePic houseTypePic);
}