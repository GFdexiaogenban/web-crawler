package house.service.impl;

import house.dao.HouseInfoDao;
import house.pojo.HouseTypePic;
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
    public void save(HouseTypePic houseTypePic) {

        // 根据每条工作信息的url和房屋名称和文章标题
        HouseTypePic param = new HouseTypePic();
        param.setUrl(houseTypePic.getUrl());
        param.setHouseName(houseTypePic.getHouseName());
        param.setHouseType(houseTypePic.getHouseType());
        param.setTypeName(houseTypePic.getTypeName());

        // 执行查询
        List<HouseTypePic> list = this.findHouseInfo(param);
        System.out.println("执行查询");
        // 判断查询结果是否为空
        if (list.size() == 0) {
            // 如果查询结果为空, 表示招聘信息数据不存在, 或者已经更新了, 需要增或者更新数据库
            this.houseInfoDao.saveAndFlush(houseTypePic);
        }
        System.out.println(2);
    }

    @Override
    public List<HouseTypePic> findHouseInfo(HouseTypePic houseTypePic) {

        // 设置查询条件
        Example example = Example.of(houseTypePic);

        // 执行查询
        List list = this.houseInfoDao.findAll(example);

        return list;
    }
}
