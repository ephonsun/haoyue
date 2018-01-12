package com.haoyue.hywebsite;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created by LiJia on 2018/1/12.
 */
public interface TopicsRepo extends BaseRepo<Topics,Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update website_topics set views=views+1 where id=?1")
    void addviews(int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from website_topics_topics_list where topics_list_id=?1")
    void delUnit(int id);
}
