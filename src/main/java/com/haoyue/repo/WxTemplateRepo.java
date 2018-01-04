package com.haoyue.repo;

import com.haoyue.pojo.WxTemplate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/29.
 */
public interface WxTemplateRepo extends BaseRepo<WxTemplate,Integer> {


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from wx_template where seller_id=?1 and active=false ")
    void delActiveFalse(String sellerId);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update wx_template set active=false  where active=true  and end_date<?1 ")
    void updateActive(Date date);

    @Query(nativeQuery = true,value = "select distinct(open_id) from wx_template where active=true")
    List<String> findActive();

    @Query(nativeQuery = true,value = "select * from wx_template where open_id=?1 and active=true")
    List<WxTemplate> findByOpenIdAndActive(String openid);
}
