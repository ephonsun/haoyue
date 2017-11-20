package com.haoyue.tuangou.web;

import com.haoyue.Exception.MyException;
import com.haoyue.tuangou.pojo.TComment;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.pojo.TuanOrders;
import com.haoyue.tuangou.service.TCommentService;
import com.haoyue.tuangou.service.TOrdersService;
import com.haoyue.tuangou.service.TuanOrdersService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TOSSClientUtil;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by LiJia on 2017/11/14.
 */
@RestController
@RequestMapping("/tuan/tcomment")
public class TCommentController {

    @Autowired
    private TCommentService commentService;
    @Autowired
    private TOrdersService ordersService;
    @Autowired
    private TuanOrdersService tuanOrdersService;


    // /tuan/tcomment/save?oid=订单ID&wxname=微信名称&wxpic=微信头像&openId=123&saleId=12&pics=评论图片
    @RequestMapping("/save")
    public TResult save(TComment comment, String oid) {
        comment.setCreateDate(new Date());
        commentService.save(comment);
        TOrders orders = ordersService.findOne(Integer.parseInt(oid));
        orders.setComment(comment);
        ordersService.update(orders);
        orders.setIscomment(true);
        comment.settProducts(orders.gettProducts());
        comment.settProductsTypes(orders.gettProductsTypes());
        commentService.save(comment);
        return new TResult(false, TGlobal.do_success, null);
    }

    // /tuan/tcomment/tuansave?oid=订单ID&wxname=微信名称&wxpic=微信头像&openId=123&saleId=12&pics=评论图片
    @RequestMapping("/tuansave")
    public TResult tuanSave(TComment comment, String oid) {
        comment.setCreateDate(new Date());
        commentService.save(comment);
        TuanOrders tuanOrders = tuanOrdersService.findOne(Integer.parseInt(oid));
        tuanOrders.setIscomment(true);
        tuanOrders.setComment(comment);
        tuanOrdersService.update(tuanOrders);
        comment.settProducts(tuanOrders.gettProducts());
        comment.settProductsTypes(tuanOrders.gettProductsTypes());
        commentService.save(comment);
        return new TResult(false, TGlobal.do_success, null);
    }

    // /tuan/tcomment/uploadpic?files=xxx
    @RequestMapping("/uploadpic")
    public TResult uploadPics(MultipartFile[] files) {
        if (files.length != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            String url = "";
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                TOSSClientUtil tossClientUtil = new TOSSClientUtil();
                try {
                    url = tossClientUtil.uploadImg2Oss(file);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(url);
                if (i != files.length - 1) {
                    stringBuffer.append(",");
                }

            }
            return new TResult(false, TGlobal.do_success, stringBuffer.toString());
        }
        return new TResult(false, TGlobal.do_success, null);
    }

    // 查看某个商品的评论
    // /tuan/tcomment/product?pid=商品ID
    @RequestMapping("/product")
    public TResult product(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<TOrders> iterable = ordersService.comments(map, pageNumber, pageSize);
        Iterable<TuanOrders> iterable2 = tuanOrdersService.comments(map, pageNumber, pageSize);
        List<TComment> list = new ArrayList<>();
        List<TComment> sortlist = new ArrayList<>();
        list.addAll(getComments1(iterable));
        list.addAll(getComments2(iterable2));
        list.stream()
                .sorted((p1, p2) -> p1.getCreateDate().compareTo(p2.getCreateDate()))
                .forEach(p -> sortlist.add(p));
        return new TResult(false, TGlobal.do_success, sortlist);
    }


    //  评论列表 /tuan/tcomment/list?saleId=123
    @RequestMapping("/list")
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<TOrders> iterable = ordersService.commentslist(map);
        Iterable<TuanOrders> iterable2 = tuanOrdersService.commentslist(map);
        List<TComment> list = new ArrayList<>();
        List<TComment> sortlist = new ArrayList<>();
        List<TComment> result = new ArrayList<>();
        list.addAll(getComments1(iterable));
        list.addAll(getComments2(iterable2));
        list.stream()
                .sorted((p1, p2) -> p1.getCreateDate().compareTo(p2.getCreateDate()))
                .forEach(p -> sortlist.add(p));
        //使用list实现分页功能
        double pagenumber = Math.ceil((list.size() / 10.0));
        if (pageNumber == 0) {
            if (pageSize > list.size()) {
                result = list;
            } else {
                result = list.subList(0, pageSize);
            }
        } else {
            if (pageNumber > pagenumber) {
                return new TResult(true, TGlobal.pagenumber_not_right, null);
            }
            if (pageNumber * pageSize + pageSize > list.size()) {
                result = list.subList(pageNumber * pageSize, list.size());
            } else {
                result = list.subList(pageNumber * pageSize, pageSize + pageNumber * pageSize);
            }
        }
        return new TResult(false, pagenumber + "", result);
    }


    //  卖家回复   /tuan/tcomment/replay?saleId=123&commentId=评论信息ID&replay=回复内容
    @RequestMapping("/replay")
    public TResult replay(String saleId, String commentId, String replay) {
        TComment comment = commentService.findOne(Integer.parseInt(commentId));
        if (!comment.getSaleId().equals(saleId)) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        comment.setReplay(replay);
        commentService.update(comment);
        return new TResult(false, TGlobal.do_success, null);
    }


    public List<TComment> getComments1(Iterable<TOrders> iterable) {
        Iterator<TOrders> iterator = iterable.iterator();
        List<TComment> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getComment());
        }
        return list;
    }

    public List<TComment> getComments2(Iterable<TuanOrders> iterable) {
        Iterator<TuanOrders> iterator = iterable.iterator();
        List<TComment> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getComment());
        }
        return list;
    }

}
