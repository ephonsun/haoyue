package com.haoyue.web;

import com.haoyue.pojo.Dictionary;
import com.haoyue.pojo.Visitors;
import com.haoyue.service.DictionaryService;
import com.haoyue.service.VisitorsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/8/24.
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private VisitorsService visitorsService;

    @RequestMapping("/findOne")
    public Result findOne(String token, String name){
        return new Result(dictionaryService.findByTokenAndName(token,name),token);
    }

    @RequestMapping("/save")
    public Result save(String token, Dictionary dictionary){
        return   dictionaryService.save(dictionary,token);
    }

    @RequestMapping("/list")
    public Result list(String token){
        return dictionaryService.findBySellerId(Integer.parseInt(token));
    }

    @RequestMapping("/del")
    public Result del(Integer id){
        return dictionaryService.del(id);
    }

    @RequestMapping("/findByDateAndSellerId")
    public Result findByDateAndSellerId(Integer id){
        return new Result(dictionaryService.findByDateAndSellerId(new Date(),id));
    }

    //整个店铺(商品)浏览量
    @RequestMapping("/addViews")
    public Result addView(String sellerId,String proId){
        if (proId==null) {
            Dictionary dictionary = dictionaryService.findByDateAndSellerId(new Date(), Integer.parseInt(sellerId));
            if (dictionary==null){
                dictionaryService.addEachDay2();
                dictionary = dictionaryService.findByDateAndSellerId(new Date(), Integer.parseInt(sellerId));
            }
            dictionary.setViews(dictionary.getViews()==null?0:dictionary.getViews() + 1);
            dictionaryService.update(dictionary);
            return new Result(false, Global.do_success, null, null);
        }
        else {
            Dictionary dictionary=dictionaryService.findByProductId(Integer.parseInt(proId));
            dictionary.setViews(dictionary.getViews()==null?0:dictionary.getViews() + 1);
            dictionaryService.update(dictionary);
            return new Result(false, Global.do_success, null, null);
        }
    }

    //整个店铺（商品）访客数
    @RequestMapping("/addVisitors")
    public Result addVisitors(String sellerId,String openId,String proId){
        if (proId==null) {
            Integer sellerId1 = Integer.parseInt(sellerId);
            Visitors visitors = visitorsService.findBySellerIdAndOpenId(sellerId1, openId);
            if (visitors == null) {
                visitors = new Visitors();
                visitors.setSellerId(sellerId1);
                visitors.setOpenId(openId);
                visitorsService.save(visitors);
                //同步代码块
                synchronized (this) {
                    Dictionary dictionary = dictionaryService.findByDateAndSellerId(new Date(), Integer.parseInt(sellerId));
                    if (dictionary==null){
                        dictionaryService.addEachDay2();
                        dictionary = dictionaryService.findByDateAndSellerId(new Date(), Integer.parseInt(sellerId));
                    }
                    dictionary.setVisitors(dictionary.getVisitors() == null ? 0 : dictionary.getVisitors() + 1);
                    dictionaryService.update(dictionary);
                }
            }
            return new Result(false, Global.do_success, null, null);
        }else {
            Visitors visitors= visitorsService.findByProductIdAndOpenId(Integer.parseInt(proId),openId);
            if (visitors == null) {
                visitors = new Visitors();
                visitors.setProductId(Integer.parseInt(proId));
                visitors.setOpenId(openId);
                visitorsService.save(visitors);
                Dictionary dictionary = dictionaryService.findByProductId(Integer.parseInt(proId));
                dictionary.setVisitors(dictionary.getVisitors()==null?0:dictionary.getVisitors() + 1);
                dictionaryService.update(dictionary);
            }
            return new Result(false, Global.do_success, null, null);
        }
    }

}
