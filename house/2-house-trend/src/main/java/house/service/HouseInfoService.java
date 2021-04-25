package house.service;

import house.pojo.HouseTrendInfo;

import java.util.List;


public interface HouseInfoService {

    /**
     * 保存房屋信息
     * @param houseTrendInfo
     */
    public void save(HouseTrendInfo houseTrendInfo);

    /**
     * 根据条件查询房屋信息
     * @param houseTrendInfo
     * @return
     */
    public List<HouseTrendInfo> findHouseInfo(HouseTrendInfo houseTrendInfo);
}