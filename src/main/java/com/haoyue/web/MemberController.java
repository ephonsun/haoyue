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
    //   /member/save?sellerId=卖家ID&lev_discount=lev1_0.9_1000,lev2_0.8_2000,lev3_0.6_3000
    @RequestMapping("/save")
    public Result save(String sellerId, String lev_discount,String comment1 ,String comment2,String comment3) {
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
            lev = s.split("_")[0];
            discount = s.split("_")[1];
            total_consume = s.split("_")[2];
            member.setLeavel(lev);
            member.setDiscount(discount);
            member.setCreateDate(new Date());
            member.setSellerId(sellerId);
            member.setTotal_consume(total_consume);
            member.setComment1(comment1);
            member.setComment2(comment2);
            member.setComment3(comment3);
            memberService.save(member);
            news.add(member);
        }
        // 更新买家会员信息 discount
        memberService.flush(news,sellerId);
        return new Result(false, Global.do_success, null, null);
    }

    //  查看买家会员信息 /member/findOne?sellerId=卖家ID&openId=12345
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


    //  查看指定卖家店铺会员体系 /member/list?sellerId=卖家Id
    @RequestMapping("/list")
    public Result list(String sellerId) {
        List<Member> memberList = memberService.findBySellerIdAndOpenIdIsNull(sellerId);
        return new Result(false, Global.do_success, memberList, null);
    }


    //   买家领取会员卡  /member/addone?openId=1234&sellerId=卖家ID&discount=折扣&wxname=微信名&leavel=等级
    @RequestMapping("/addone")
    public Result addVip(Member member){
        //判断是否已领当前等级会员卡
        Member old=memberService.findByOpenIdAndLeavelAndSellerId(member.getOpenId(),member.getLeavel(),member.getSellerId());
        if (old!=null){
            return new Result(true, Global.member_exist, null, null);
        }
        old=memberService.findByOpenIdAndSellerId(member.getOpenId(),member.getSellerId());
        if (old!=null){
            member.setCode(old.getCode());
            member.setId(old.getId());
        }
        member.setCreateDate(new Date());
        memberService.save(member);
        //会员号
        if (StringUtils.isNullOrBlank(member.getCode())) {
            member.setCode("888" + member.getId());
            memberService.save(member);
        }
        return new Result(false, Global.do_success, null, null);
    }

}
