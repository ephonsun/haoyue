package com.haoyue.web;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.Integral;
import com.haoyue.pojo.IntegralRecord;
import com.haoyue.pojo.SignIn;
import com.haoyue.service.CustomerService;
import com.haoyue.service.IntegralRecordService;
import com.haoyue.service.IntegralService;
import com.haoyue.service.SignInService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Lijia on 2018/4/8.
 */
@RestController
@RequestMapping("/signin")
public class SignInController {

    @Autowired
    private SignInService signInService;
    @Autowired
    private IntegralService integralService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private IntegralRecordService integralRecordService;


    // https://www.cslapp.com/signin/save?openId=123&sellerId=3&wxname=微信名称
    @RequestMapping("/save")
    public Result save(SignIn signin){
       signin.setCreateDate(new Date());
       boolean flag= signInService.findIsSignIn(signin);
       if(flag){
           return new Result(false, Global.already_signin,null,null);
       }
       //查询当前卖家设置的积分
        Map map=new HashMap();
        map.put("sellerId",signin.getSellerId());
        map.put("typename","0");
        Iterable<Integral> iterable= integralService.list(map);
        Iterator<Integral> iterator= iterable.iterator();
        while (iterator.hasNext()){
            //获取连续签到天数对应的积分
            Integral integral=iterator.next();
            if(integral.getActive()==false){
                return new Result(true, Global.activity_closed,null,null);
            }
            signInService.save(signin);
            String days=integral.getDays();
            String scrolls=integral.getScrolls();
            List<SignIn> signInList= signInService.findByOpenIdAndSellerIdAndActive(signin.getOpenId(),signin.getSellerId(),true);
            //判断是否连续签到指定天数
            if(signInList!=null&&signInList.size()==Integer.parseInt(days)){
                //更新积分
               Customer customer= customerService.findByOpenId(signin.getOpenId(),signin.getSellerId());
               customer.setUnuseScroll(customer.getUnuseScroll()+Integer.parseInt(scrolls));
               customerService.update(customer);
               //增加积分的记录
                IntegralRecord integralRecord=new IntegralRecord();
                integralRecord.setCreateDate(new Date());
                integralRecord.setFroms("0");//签到
                integralRecord.setOpenId(customer.getOpenId());
                integralRecord.setSellerId(customer.getSellerId());
                integralRecord.setScrolls(Integer.parseInt(scrolls));
                integralRecord.setType("0");//获取积分
                integralRecordService.save(integralRecord);
               //更新签到记录active=false
               for(SignIn signIn:signInList){
                   signIn.setActive(false);
                   signInService.update(signIn);
               }
            }
        }
       return new Result(false, Global.do_success,signin,null);
    }

    // /signin/list?sellerId=3&openId=123&active=true/false
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable<SignIn> iterable= signInService.list(map,pageNumber,pageSize);
        return new Result(false, Global.do_success,iterable,null);
    }

    // /signin/signInToday?sellerId=3&openId=123
    @RequestMapping("/signInToday")
    public Result signInToday(SignIn signin){
        boolean flag= signInService.findIsSignIn(signin);
        return new Result(false, Global.do_success,flag,null);
    }
}
