package com.haoyue.service;

import com.haoyue.pojo.Comment;
import com.haoyue.pojo.Order;
import com.haoyue.repo.CommentRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by LiJia on 2017/8/24.
 *
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;



    public Comment findOne(Integer id) {
        return commentRepo.findOne(id);
    }


    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }

    public List<Comment> findByPid(String pid) {
        return commentRepo.findByPid(pid);
    }

    public List<Comment> findBySellerId(String sellerId) {
        return commentRepo.findBySellerId(Integer.parseInt(sellerId));
    }
}
