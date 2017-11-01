package com.haoyue.service;

import com.haoyue.pojo.Member;
import com.haoyue.repo.MemberRepo;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LiJia on 2017/10/31.
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;

    public void save(Member member) {
        //默认0.9折
        if (StringUtils.isNullOrBlank(member.getDiscount())){
            member.setDiscount("0.9");
        }
        member.setCreateDate(new Date());
        memberRepo.save(member);
        int code_begin=88800000;
        member.setCode((code_begin+member.getId())+"");
        memberRepo.save(member);
    }

    public Member findByOpenIdAndSellerId(String openId,String sellerId){
        return memberRepo.findByOpenIdAndSellerId(openId,sellerId);
    }

    public void update(String sellerId, String discount) {
        memberRepo.updateDiscount(sellerId,discount);
    }

    public String getDiscount(String sellerId) {
        return memberRepo.getDiscount(sellerId);
    }

    public Member findBySellerIdAndOpenIdIsNull(String sellerId) {
        return memberRepo.findBySellerIdAndOpenIdIsNull(sellerId);
    }
}
