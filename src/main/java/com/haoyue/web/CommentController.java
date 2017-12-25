package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Comment;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.Seller;
import com.haoyue.service.CommentService;
import com.haoyue.service.OrderService;
import com.haoyue.service.SellerService;
import com.haoyue.untils.Global;
import com.haoyue.untils.OSSClientUtil;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/24.
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;



    @RequestMapping("/reply")
    public Result reply(Integer id, String token, String message) {
        if (message.length() > 250) {
            return new Result(true, Global.message_tolong, token);
        }
        Comment comment = commentService.findOne(id);
        comment.setReversion(message);
        commentService.save(comment);
        return new Result(false, Global.do_success, token);
    }

    //   /comment/save?level=好评/中评/差评&message=评论内容&orderId=订单ID&openId=123&wxname=微信名称&sellerId=卖家ID&images=评论图片外链地址
    @RequestMapping("/save")
    public Result save(Comment comment) {
        Order order=orderService.findOne(comment.getOrderId());
        comment.setCreateDate(new Date());
        comment.setPid(order.getProducts().get(0).getId()+"");
        commentService.save(comment);
        order.setComment(comment);
        order.setIscomment(true);
        orderService.update(order);
        return new Result(false, Global.do_success, null, null);
    }

    //  /comment/uploadPics?multipartFiles=图片&sellerId=卖家ID
    @RequestMapping("/uploadPics")
    public Result uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {

        Seller seller = sellerService.findOne(sellerId);
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                int kb = (int) (multipartFile.getSize() / 1024);
                seller.setUploadFileSize(seller.getUploadFileSize() + kb);
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href+uploadUrl);
                stringBuffer.append(",");
            }
        }
        sellerService.update2(seller);
        return new Result(false, Global.do_success, stringBuffer.toString(), null);
    }


    // 查看指定商品的评论 /comment/pid?sellerId=卖家ID&pid=商品ID
    @RequestMapping("/pid")
    public Result findByProduct(String pid){
        List<Comment> list= commentService.findByPid(pid);
        if (list!=null&&list.size()!=0) {
            Collections.reverse(list);
        }
        return new Result(false, Global.do_success, list, null);
    }

    //  指定买家待评价订单列表 /comment/uncomments?sellerId=卖家ID&openId=123
    @RequestMapping("/uncomments")
    public Result uncomment(String openId,String sellerId){
        List<Order> list=orderService.findUnComment(openId,sellerId);
        return new Result(false, Global.do_success, list, null);
    }

}
