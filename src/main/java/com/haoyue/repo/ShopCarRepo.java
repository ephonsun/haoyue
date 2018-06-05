package com.haoyue.repo;

import com.haoyue.pojo.ShopCar;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/9/4.
 */
public interface ShopCarRepo extends BaseRepo<ShopCar, Integer> {

    @Query(nativeQuery = true, value = "select count(*) from shopcar_productses where productses_id=?1")
    int findCountByPid(Integer id);

    @Query(nativeQuery = true, value = "select shop_car_id from shopcar_productses where productses_id=?1")
    List<Integer> findShopCarIdByProId(String proId);

    @Query(nativeQuery = true, value = "select * from shopcar where seller_id=?1 and create_date > ?2 and create_date<?3")
    List<ShopCar> findBySellerIdAndCreateDate(String sellerId, Date from, Date end);

    @Query(nativeQuery = true,value = "select  * from shopcar where seller_id=?1 and active=true")
    List<ShopCar> findActiveIsTrue(Integer sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update  shopcar set active=false  where id=?1")
    void updateActiveFalse(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update  shopcar set active=false  where end_date<?1 and active=true")
    void updateActiveFalseByDate(Date date);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update  shopcar set form_id2=null  where id=?1")
    void updateFormId2(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update  shopcar set form_id=''  where id=?1")
    void updateFormId(Integer id);
}
