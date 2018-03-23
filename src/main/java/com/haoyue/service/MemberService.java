package com.haoyue.service;

import com.haoyue.pojo.Member;
import com.haoyue.pojo.QMember;
import com.haoyue.repo.MemberRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    public Member findByOpenIdAndLeavelAndSellerId(String openId, String leavel, String sellerId) {
        return memberRepo.findByOpenIdAndLeavelAndSellerId(openId,leavel,sellerId);
    }

    public void flush(List<Member> news, String sellerId) {
        String discount="";
        String leavel="";
        for (Member member:news){
            discount=member.getDiscount();
            leavel=member.getLeavel();
            memberRepo.flushInfo(discount,leavel,sellerId);
        }
    }

    public Member findBySellerIdAndLeavel(String sellerId, String leavel) {
        return  memberRepo.findBySellerIdAndLeavel(sellerId,leavel);
    }

    public Iterable<Member> list(Map<String, String> map, int pageNumber, int pageSize) {

        QMember member=QMember.member;
        BooleanBuilder bd=new BooleanBuilder();
        int numsfrom=0;
        int numsto=0;
        double expensefrom=0;
        double expenseto=0;
        Date datefrom=null;
        Date dateto=null;
        Date birthfrom=null;
        Date birthto=null;

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

                if(name.equals("groupname")){
                    bd.and(member.groupName.eq(value));
                }
                else if(name.equals("wxname")){
                    bd.and(member.wxname.eq(value));
                }
                else if(name.equals("sellerId")){
                    bd.and(member.sellerId.eq(value));
                }
                else if(name.equals("lev")){
                    bd.and(member.leavel.eq(value));
                }
                else if(name.equals("sex")){
                    bd.and(member.sex.eq(value));
                }
                else if(name.equals("discount")){
                    bd.and(member.discount.eq(value));
                }
                else if(name.equals("nums_from")){
                    numsfrom=Integer.parseInt(value);
                }
                else if(name.equals("nums_to")){
                    numsto=Integer.parseInt(value);
                }
                else if(name.equals("expense_from")){
                    expensefrom=Double.valueOf(value);
                }
                else if(name.equals("expense_to")){
                    expenseto=Double.valueOf(value);
                }
                else if(name.equals("datefrom")){
                    try {
                        datefrom=StringUtils.formatDate2(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(name.equals("dateto")){
                    try {
                        dateto=StringUtils.formatDate2(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(name.equals("birthfrom")){
                    try {
                        birthfrom=StringUtils.formatDate2(new Date().getYear()+"-"+value);//2018-10-10
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(name.equals("birthto")){
                    try {
                        birthto=StringUtils.formatDate2(new Date().getYear()+"-"+value);//2018-10-10
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
        //消费次数
        if(numsto!=0){
            bd.and(member.nums.between(numsfrom,numsto));
        }
        //消费额
        if(expenseto!=0){
            bd.and(member.total_consume.between(expensefrom,expenseto));
        }
        //上次交易时间
        if(datefrom!=null&&dateto!=null){
            bd.and(member.latestBuyDate.between(datefrom,dateto));
        }
        //生日
        if(birthfrom!=null&&birthto!=null){
           bd.and(member.birthDate.between(birthfrom,birthto));
        }
        return memberRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));

    }

    public Member findById(int id) {
        return memberRepo.findOne(id);
    }




}
