package com.haoyue.service;

import com.haoyue.pojo.Comment;
import com.haoyue.pojo.Order;
import com.haoyue.repo.CommentRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *
 * Created by LiJia on 2017/8/24.
 *
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;

    public Comment findOne(Integer id) {
        return commentRepo.findOne(id);
    }

    public void reply(Comment comment) {
        commentRepo.save(comment);
    }

    public Object save(String openId, Comment comment) {

        Order order=orderService.findOne(comment.getOrderId());
        //Integer customerId=customerService.findByOpenId(openId).getId();
        Integer customerId=null;
        if (order.getCustomerId()!=customerId){
            return new Result(true, Global.do_fail,null,null);
        }
        comment.setCustomerId(customerId);
        comment.setCreateDate(new Date());
        Comment commont1=commentRepo.save(comment);
        order.setComment(commont1);
        orderService.update(order);
        return commont1;
    }


}
