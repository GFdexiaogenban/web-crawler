package jd.service;

import jd.pojp.Item;

import java.util.List;

public interface ItemService {
    //保存商品
    public void save(Item item);

    //查询方法
    public List<Item> findAll(Item item);
}
