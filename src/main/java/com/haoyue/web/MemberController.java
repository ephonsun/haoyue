package com.haoyue.web;

import com.haoyue.pojo.Member;
import com.haoyue.service.MemberService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/10/31.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/save")
    public Result save(Member member){
        //检验数据是否存在
        Member member1=memberService.findByOpenIdAndSellerId(member.getOpenId(),member.getSellerId());
        if (member1!=null&&member1.getId()!=null){
            return new Result(true, Global.member_exist,null,null);
        }
        memberService.save(member);
        return new Result(false, Global.do_success,null,null);
    }

    @RequestMapping("/update")
    public Result update(String sellerId,String discount){
        Member member=memberService.findBySellerIdAndOpenIdIsNull(sellerId);
        if(member==null){
            member=new Member();
            member.setDiscount(discount);
            member.setSellerId(sellerId);
            memberService.save(member);
        }else {
            memberService.update(sellerId, discount);
        }
        return new Result(false, Global.do_success,null,null);
    }

    @RequestMapping("/findOne")
    public Result findOne(String sellerId,String openId){
        Member member=memberService.findByOpenIdAndSellerId(openId,sellerId);
        return new Result(false, Global.do_success,member,null);
    }

    @RequestMapping("/getDiscount")
    public Result getDiscount(String sellerId){
        String discount=memberService.getDiscount(sellerId);
        if (StringUtils.isNullOrBlank(discount)){
            return new Result(false, Global.discount_not_set,null,null);
        }
        return new Result(false, Global.do_success,discount,null);
    }

}
