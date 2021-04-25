package house.service.impl;

import house.dao.HouseInfoDao;
import house.pojo.HouseDetail;
import house.service.HouseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HouseInfoServiceImpl implements HouseInfoService {

    @Autowired
    private HouseInfoDao houseInfoDao;

    @Override
    @Transactional
    public void save(HouseDetail houseDetail) {

        // 根据每条工作信息的url和发布时间查询数据
        HouseDetail param = new HouseDetail();
        param.setUrl(houseDetail.getUrl());
        param.setHouseName(houseDetail.getHouseName());

        // 执行查询
        List<HouseDetail> list = this.findHouseInfo(param);
        System.out.println(1);
        // 判断查询结果是否为空
        if (list.size() == 0) {
            // 如果查询结果为空, 表示招聘信息数据不存在, 或者已经更新了, 需要增或者更新数据库
            this.houseInfoDao.saveAndFlush(houseDetail);
        }
        System.out.println(2);
    }

    @Override
    public List<HouseDetail> findHouseInfo(HouseDetail houseDetail) {

        // 设置查询条件
        Example example = Example.of(houseDetail);

        // 执行查询
        List list = this.houseInfoDao.findAll(example);

        return list;
    }
}
