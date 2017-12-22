package com.haoyue.repo;

import com.haoyue.pojo.Comment;
import com.haoyue.repo.BaseRepo;

import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
public interface CommentRepo extends BaseRepo<Comment,Integer> {
    List<Comment> findByPid(String pid);

    List<Comment> findBySellerId(Integer sellerId);
}
