package house.service;

import house.pojo.HousePhoto;

import java.util.List;


public interface HouseInfoService {

    /**
     * 保存房屋信息
     * @param housePhoto
     */
    public void save(HousePhoto housePhoto);

    /**
     * 根据条件查询房屋信息
     * @param housePhoto
     * @return
     */
    public List<HousePhoto> findHouseInfo(HousePhoto housePhoto);
}