package com.haoyue.repo;

import com.haoyue.pojo.CustomProductsTypes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by LiJia on 2017/12/8.
 */
public interface CustomProductsTypesRepo extends BaseRepo<CustomProductsTypes,Integer> {
    List<CustomProductsTypes> findBySellerId(String sellerId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from customer_pro_types_childs where childs_id=?1")
    void del_middle(Integer id);
}
