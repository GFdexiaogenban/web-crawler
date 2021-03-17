package jd.Dao;

import jd.pojp.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItenDao extends JpaRepository<Item,Long> {

}
