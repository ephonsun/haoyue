package com.haoyue.web;

import com.haoyue.pojo.Integral;
import com.haoyue.pojo.Seller;
import com.haoyue.service.IntegralService;
import com.haoyue.service.SellerService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lijia on 2018/4/8.
 * 卖家后台设置积分
 */
@RestController
@RequestMapping("/integral")
public class IntegralController {

    @Autowired
    private IntegralService integralService;



    //  /integral/save?sellerId=3&typename=0（签到获得积分 0 交易获得积分 1）&days=连续签到天数&expense=单次消费金额
    //  &scrolls=奖励积分&comments=活动说明（&id=当前更新的ID）
    @RequestMapping("/save")
    public Result save(Integral integral){
        //判断新增是否已经存在
        if(integral.getId()==null){
            Map map=new HashMap();
            map.put("sellerId",integral.getSellerId());
            Iterable<Integral> iterable= integralService.list(map);
            Iterator<Integral> iterator= iterable.iterator();
            while (iterator.hasNext()){
                if(iterator.next().getTypename().equals(integral.getTypename())){
                    return new Result(false, Global.record_exist,null,null);
                }
            }
        }
        integral.setCreateDate(new Date());
        integralService.save(integral);
        return new Result(false, Global.do_success,integral,null);
    }

    //   /integral/active?id=当前操作的记录Id&active=true/false
    @RequestMapping("/active")
    public Result active(int id ,boolean active){
        Integral integral= integralService.findById(id);
        integral.setActive(active);
        return new Result(false, Global.do_success,integral,null);
    }

    //   /integral/list?sellerId=3(&typename=0/1)
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map){
        Iterable<Integral> integralIterable=integralService.list(map);
        return new Result(false, Global.do_success,integralIterable,null);
    }

}
