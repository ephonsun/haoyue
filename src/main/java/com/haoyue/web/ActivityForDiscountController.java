package com.haoyue.web;

import com.haoyue.pojo.ActivityForDiscount;
import com.haoyue.pojo.Products;
import com.haoyue.pojo.ProdutsType;
import com.haoyue.service.ActivityForDiscountService;
import com.haoyue.service.ProductsService;
import com.haoyue.service.ProdutsTypeService;
import com.haoyue.service.ShopCarService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/4/23.
 *
 *商品折扣活动
 *
 */

@RestController
@RequestMapping("/activity/discount")
public class ActivityForDiscountController {


    @Autowired
    private ActivityForDiscountService activityForDiscountService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private ProdutsTypeService produtsTypeService;


//     /activity/discount/save?sellerId=3&activityName=活动名称&activitylabel=活动标签
//       fromdate=活动开始日期&todate=活动结束日期(时间格式  2017-9-19 16:28:25)
    @RequestMapping("/save")
    public Result save(ActivityForDiscount activityForDiscount,String  fromdate,String todate) throws ParseException {
        activityForDiscount.setCreateDate(new Date());
        activityForDiscount.setFromDate(StringUtils.formatDate2(fromdate));
        activityForDiscount.setEndDate(StringUtils.formatDate2(todate));
        if(activityForDiscount.getFromDate().after(activityForDiscount.getEndDate())){
            return new Result(true, Global.date_unright,null,null);
        }
        activityForDiscountService.save(activityForDiscount);
        return new Result(false, Global.do_success,activityForDiscount,null);
    }


    //  绑定折扣活动和商品
    // /activity/discount/bind?activityId=活动ID&pid=商品ID(不是商品分类ID)&sellerId=3
    @RequestMapping("/bind")
    public Result bindActivityWithProduct(int activityId,int pid,String sellerId){
        Products products= productsService.findOne(pid);
        ActivityForDiscount activityForDiscount= activityForDiscountService.findOne(activityId);
        products.setActivityForDiscount(activityForDiscount);
        productsService.update(products);



        return new Result(false, Global.do_success,null,null);
    }

    // /activity/discount/unbind?pid=商品ID(不是商品分类ID)&sellerId=3
    @RequestMapping("/unbind")
    public Result unbind(int pid,String sellerId){
        Products products= productsService.findOne(pid);
        products.setActivityForDiscount(null);
        productsService.update(products);
        List<ProdutsType> produtsTypes=products.getProdutsTypes();
        for (ProdutsType type:produtsTypes){
            type.setDiscountPrice(0.0);
            type.setISDiscount(false);
        }
        produtsTypeService.update(produtsTypes);
        return new Result(false, Global.do_success,null,null);
    }

    //  /activity/discount/findone?id=12&sellerId=3
    @RequestMapping("/findone")
    public Result findOne(int id,String sellerId){
        ActivityForDiscount activityForDiscount= activityForDiscountService.findOne(id);
        Iterable<Products> productsList=productsService.findByActivityDiscount(activityForDiscount.getId());
        List<Object> list=new ArrayList<>();
        list.add(activityForDiscount);
        list.add(productsList);
        return new Result(false, Global.do_success,list,null);
    }

    //  /activity/discount/del?id=12&sellerId=3
    @RequestMapping("/del")
    public Result del(int id,String sellerId){
        ActivityForDiscount activityForDiscount= activityForDiscountService.findOne(id);
        activityForDiscount.setActive(false);
        activityForDiscountService.update(activityForDiscount);
        return new Result(false, Global.do_success,activityForDiscount,null);
    }

//     所有列表 /activity/discount/list?sellerId=3&active=true
//     未开始列表 /activity/discount/list?sellerId=3&before=yes&active=true
//     已经结束列表 /activity/discount/list?sellerId=3&after=yes&active=true
//     正在进行列表 /activity/discount/list?sellerId=3&between=yes&active=true
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<ActivityForDiscount> iterable = activityForDiscountService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }




}
