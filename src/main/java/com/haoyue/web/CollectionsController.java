package com.haoyue.web;

import com.haoyue.pojo.Collections;
import com.haoyue.service.CollectionsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
@RestController
@RequestMapping("/collection")
public class CollectionsController {

    @Autowired
    private CollectionsService collectionsService;


    //   /collection/save?openId=12&sellerId=12&wxname=微信名&wxpic=微信头像&pid=商品Id
    @RequestMapping("/save")
    public Result save(Collections collections) {
        collections.setCreateDate(new Date());
        collectionsService.save(collections);
        return new Result(false, Global.do_success,null,null);
    }

    //   /collection/cancel?openId=12&id=收藏记录Id
    @RequestMapping("/cancel")
    public Result cancel(String id,String openId){
        Collections collections=collectionsService.findOne(Integer.parseInt(id));
        if (!collections.getOpenId().equals(openId)){
            return new Result(true, Global.have_no_right,null,null);
        }
        collectionsService.del(collections);
        return new Result(false, Global.do_success,null,null);
    }


    //   /collection/clist?openId=12&sellerId=123
    @RequestMapping("/clist")
    public Result customerlist(String openId,String sellerId){
        List<Collections> list= collectionsService.findByOpenIdAndSellerId(openId,sellerId);
        return new Result(false, Global.do_success,list,null);
    }


    //   /collection/slist?sellerId=123
    @RequestMapping("/slist")
    public Result list(String sellerId){
        Date now=new Date();
        Date date= StringUtils.getYMD(now);
        //今日收藏量
        List<Collections> list1=collectionsService.findBySellerIdAndCreateDate(sellerId,date);
        //昨日收藏量
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        List<Collections> list2=collectionsService.findBySellerIdAndCreateDate(sellerId,calendar.getTime());
        //封装结果集
        Response response=new Response();
        response.setSum(list1.size());
        //计算比率  前端将返回的比例 -1 即可 正数为上升比例 负数为下降比例
        if (list1.size()==0){
            if (list2.size()==0){
                response.setRate(1);
            }else {
                response.setRate(0);
            }
        }else {
            if (list2.size()==0){
                response.setRate(2);
            }else {
                response.setRate(list1.size()/list2.size());
            }
        }
        return new Result(false, Global.do_success,response,null);
    }

    class  Response{
        private int sum;
        private double rate;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }


}
