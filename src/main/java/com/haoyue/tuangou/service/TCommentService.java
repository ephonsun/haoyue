package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.QTOrders;
import com.haoyue.tuangou.pojo.TComment;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.repo.TCommentRepo;
import com.haoyue.tuangou.repo.TOrdersRepo;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/11/14.
 */
@Service
public class TCommentService {

    @Autowired
    private TCommentRepo commentRepo;



    public void save(TComment comment) {
        commentRepo.save(comment);
    }


    public TComment findOne(int id) {
        return commentRepo.findOne(id);
    }

    public void update(TComment comment) {
        commentRepo.save(comment);
    }

    public void updateWxname(String openId,String wxname){
        commentRepo.updateWxname(openId,wxname);
    }

    public void updateWxpic(String openId,String wxpic){
        commentRepo.updateWxpic(openId,wxpic);
    }

}
