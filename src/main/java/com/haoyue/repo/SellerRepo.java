package com.haoyue.repo;

import com.haoyue.pojo.Seller;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LiJia on 2017/8/21.
 */
public   interface SellerRepo extends BaseRepo<Seller,Integer> {


    Seller findBySellerNameAndSellerPass(String sellerName, String sellerPass);

    Seller findBySellerPassAndSellerEmail(String sellerPass, String sellerName);

    Seller findBySellerPassAndSellerPhone(String sellerPass, String sellerName);

    @Query(nativeQuery = true,value = "select seller_id from sellers")
    List<Integer> findIds();

    Seller findBySellerPhone(String phone);

    List<String> findByOnlineCode(String online_code);
}
