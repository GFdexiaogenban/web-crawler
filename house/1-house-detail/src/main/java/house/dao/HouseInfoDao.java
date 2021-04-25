package house.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import house.pojo.HouseDetail;

public interface HouseInfoDao extends JpaRepository<HouseDetail,Long> {
}

