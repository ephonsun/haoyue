package com.haoyue.service;

import com.haoyue.pojo.Member;
import com.haoyue.repo.MemberRepo;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by LiJia on 2017/10/31.
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;

    public void save(Member member) {
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

    public List<Member> findBySellerIdAndOpenIdIsNull(String sellerId) {
        return memberRepo.findBySellerIdAndOpenIdIsNull(sellerId);
    }



    public List<Member> list(String sellerId) {
        return memberRepo.findBySellerIdAndOpenIdIsNotNull(sellerId);
    }

    public void updateMemeber(Member member) {
        memberRepo.save(member);
    }

    public void del(Member member) {
        memberRepo.delete(member);
    }

    public void delVip() {
        memberRepo.delVip();
    }

    public List<Member> findByOpenIdIsNotNull() {
        return memberRepo.findByOpenIdIsNotNull();
    }
}
