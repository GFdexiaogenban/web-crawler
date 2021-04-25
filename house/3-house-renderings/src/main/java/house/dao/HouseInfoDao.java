package house.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import house.pojo.HouseTypePic;

public interface HouseInfoDao extends JpaRepository<HouseTypePic,Long> {
}

