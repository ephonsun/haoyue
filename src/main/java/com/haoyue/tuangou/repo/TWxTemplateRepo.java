package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TWxTemplate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/17.
 */
public interface TWxTemplateRepo extends TBaseRepo<TWxTemplate,Integer>{

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_wx_template set active=false where active=true and end_date<?1")
    void autoFlush(Date date);

    @Query(nativeQuery = true,value = "select distinct(open_id) from t_wx_template")
    List<String> findDistinctOpenId();

    @Query(nativeQuery = true,value = "select * from t_wx_template where active=true and open_id=?1")
    List<TWxTemplate> findByActiveAndOpneId(String openid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_wx_template set active=false where id=?1")
    void updateActive(Integer id);
}
