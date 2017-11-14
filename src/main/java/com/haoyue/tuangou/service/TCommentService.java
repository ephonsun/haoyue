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


}
