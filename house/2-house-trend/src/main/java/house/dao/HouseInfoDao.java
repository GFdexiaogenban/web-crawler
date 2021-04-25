package house.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import house.pojo.HouseTrendInfo;

public interface HouseInfoDao extends JpaRepository<HouseTrendInfo,Long> {
}

