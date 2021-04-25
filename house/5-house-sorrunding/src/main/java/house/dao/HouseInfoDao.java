package house.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import house.pojo.HousePhoto;

public interface HouseInfoDao extends JpaRepository<HousePhoto,Long> {
}

