package com.haoyue.repo;

import com.haoyue.pojo.ShopCar;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LiJia on 2017/9/4.
 */
public interface ShopCarRepo extends BaseRepo<ShopCar,Integer> {

    @Query(nativeQuery = true,value = "select count(*) from shopcar_productses where productses_id=?1")
    int findCountByPid(Integer id);

    @Query(nativeQuery = true,value = "select shop_car_id from shopcar_productses where productses_id=?1")
    List<Integer> findShopCarIdByProId(String proId);
}
