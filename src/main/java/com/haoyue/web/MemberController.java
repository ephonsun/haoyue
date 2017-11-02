package com.haoyue.web;

import com.haoyue.pojo.Member;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.OrderTotalPrice;
import com.haoyue.service.MemberService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/10/31.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;

    //  卖家后台会员设置 会员等级-折扣-消费额度
    //  http://localhost:8080/member/save?sellerId=1&lev-discount=lev1-0.9-1000,lev2-0.8-2000,lev3-0.6-3000
    @RequestMapping("/save")
    public Result update(String sellerId, String lev_discount) {
        //如果存在删除原来的数据
        List<Member> members = memberService.findBySellerIdAndOpenIdIsNull(sellerId);
        if(members!=null&members.size()!=0){
            for (Member member:members){
                memberService.del(member);
            }
        }
        String[] strs = lev_discount.split(",");
        String lev = "";
        String discount = "";
        String total_consume = "";
        List<Member> news=new ArrayList<>();
        for (String s : strs) {
            Member member = new Member();
            lev = s.split("-")[0];
            discount = s.split("-")[1];
            total_consume = s.split("-")[2];
            member.setLeavel(lev);
            member.setDiscount(discount);
            member.setCreateDate(new Date());
            member.setSellerId(sellerId);
            member.setTotal_consume(total_consume);
            memberService.save(member);
            news.add(member);
        }
        // 更新买家会员信息
        orderService.getToTalPriceByCustomer(sellerId,news);
        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/findOne")
    public Result findOne(String sellerId, String openId) {
        Member member = memberService.findByOpenIdAndSellerId(openId, sellerId);
        return new Result(false, Global.do_success, member, null);
    }

    @RequestMapping("/getDiscount")
    public Result getDiscount(String sellerId) {
        String discount = memberService.getDiscount(sellerId);
        if (StringUtils.isNullOrBlank(discount)) {
            return new Result(false, Global.discount_not_set, null, null);
        }
        return new Result(false, Global.do_success, discount, null);
    }

    @RequestMapping("/list")
    public Result list(String sellerId) {
        List<Member> memberList = memberService.list(sellerId);
        return new Result(false, Global.do_success, memberList, null);
    }

}
