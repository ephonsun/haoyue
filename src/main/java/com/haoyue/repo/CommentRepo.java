package com.haoyue.repo;

import com.haoyue.pojo.Comment;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
public interface CommentRepo extends BaseRepo<Comment,Integer> {
    List<Comment> findByPid(String pid);

    List<Comment> findBySellerId(Integer sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update comments set wxname=?1 ,wxpic=?2 where seller_id=?4 and open_id=?3")
    void updateWxname(String wxname, String wxpic, String openId, String sellerId);
}
