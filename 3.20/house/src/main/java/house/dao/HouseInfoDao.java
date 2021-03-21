package house.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import house.pojo.HouseInfo;

public interface HouseInfoDao extends JpaRepository<HouseInfo,Long> {
}

