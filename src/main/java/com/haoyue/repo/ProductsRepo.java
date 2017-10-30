package com.haoyue.repo;

import com.haoyue.pojo.Products;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LiJia on 2017/8/22.
 */

public interface ProductsRepo extends BaseRepo<Products,Integer> {
    Products findByPcode(String pcode);

    @Query(nativeQuery = true,value = "select distinct(ptype_name) from products where seller_id=?1 and active=true")
    List<String> findBySellerIdAndActive(Integer sellerId);
}
