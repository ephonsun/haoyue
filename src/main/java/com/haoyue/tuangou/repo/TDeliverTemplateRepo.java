package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TDeliverTemplate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/11/7.
 */
public interface TDeliverTemplateRepo extends TBaseRepo<TDeliverTemplate,Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from t_deliver_templates_templates where templates_id=?1")
    void deleteById(Integer id);
}
