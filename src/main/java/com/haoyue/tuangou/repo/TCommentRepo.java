package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TComment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/11/14.
 */
public interface TCommentRepo extends TBaseRepo<TComment,Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_comment set wxpic=?2 where open_id=?1")
    void updateWxpic(String openId, String wxpic);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_comment set wxname=?2 where open_id=?1")
    void updateWxname(String openId, String wxname);

}
