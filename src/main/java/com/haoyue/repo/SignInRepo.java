package com.haoyue.repo;

import com.haoyue.pojo.SignIn;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Lijia on 2018/4/8.
 */
public interface SignInRepo extends BaseRepo<SignIn,Integer> {

    @Query(nativeQuery = true,value = "select * from signin where open_id=?1 and seller_id=?2 and create_date>?3")
    SignIn findIsSignIn(String openId, String sellerId, Date createDate);


    List<SignIn> findByOpenIdAndSellerIdAndActive(String openId, String sellerId, boolean b);
}
