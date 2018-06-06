package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Member;
import com.haoyue.service.MemberService;
import com.haoyue.untils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2017/10/31.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 卖家后台设置会员 - 新
    // /member/save_new?sellerId=3&total_consume=交易额&nums=交易次数
    // &discount=折扣&pic=图片地址&leavel=等级（lev1 lev2 lev3 lev4）&active=true/false
    //  &defultPic=默认会员图片地址
    @RequestMapping("/save_new")
    public Result save_new(Member member) {
        Member oldone = memberService.findBySellerIdAndLeavel(member.getSellerId(), member.getLeavel());
        if (oldone == null) {
            oldone = new Member();
            oldone.setSellerId(member.getSellerId());
            oldone.setCreateDate(new Date());
        }
        oldone.setActive(member.getActive());
        oldone.setTotal_consume(member.getTotal_consume());
        oldone.setNums(member.getNums());
        oldone.setDiscount(member.getDiscount());
        oldone.setPic(member.getPic());
        oldone.setDefultPic(member.getDefultPic());
        oldone.setLeavel(member.getLeavel());
        memberService.save(oldone);
        return new Result(false, Global.do_success, null, null);
    }


    //上传图片  /member/uploadPics
    @RequestMapping("/uploadPics")
    public UploadSuccessResult uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                stringBuffer.append(",");
            }
        }
        return new UploadSuccessResult(stringBuffer.toString());
    }


    //  查看买家会员信息 /member/findOne?sellerId=卖家ID&openId=12345
    //  /member/findOne?id=当前记录的ID&sellerId=12
    //  /member/findOne?sellerId=12&leavel=lev1
    @RequestMapping("/findOne")
    public Result findOne(String sellerId, String openId, String id,String leavel) {
        Member member = null;
        if (!StringUtils.isNullOrBlank(id)) {
            member = memberService.findById(Integer.parseInt(id));
        }
        else  if(!StringUtils.isNullOrBlank(leavel)){
            member = memberService.findBySellerIdAndLeavel(sellerId, leavel);
        }
        else {
            member = memberService.findByOpenIdAndSellerId(openId, sellerId);
        }
        return new Result(false, Global.do_success, member, null);
    }

    //  https://www.cslapp.com/member/list?sellerId=3
//     会员列表 /member/list?sellerId=3&pageNumber=当前页(从0开始)&groupname=分组名称&wxname=微信名称&lev=会员级别
//      &expense_from=交易额开始&expense_to=交易额结束&nums_from=交易笔数开始&nums_to=交易笔数结束
//      &datefrom=上次交易时间开始&dateto=上次交易时间结束&sex=性别&birthfrom=生日开始&birthto=生日结束
//      &discount=折扣
    //  昨日新增 /member/list?sellerId=3&createdate=yes
    //  会员总数 /member/list?sellerId=3
    @RequestMapping("/list")
    public Result list_new(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Member> iterable = memberService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }

    // /member/update?id=当前会员的Id&sellerId=3&wxname=微信昵称&sex=性别&phone=电话&email=邮箱
    //  &birthday=生日(1999-08-15)&province=省份&city=城市&receiveAddress=收货地址&lev=会员级别(lev1 lev2...)
    @RequestMapping("/update")
    public Result update(Member member) {
        Member old = memberService.findById(member.getId());
        old.setWxname(member.getWxname());
        old.setSex(member.getSex());
        old.setPhone(member.getPhone());
        old.setEmail(member.getEmail());
        old.setBirthday(member.getBirthday());
        old.setProvince(member.getProvince());
        old.setCity(member.getCity());
        old.setReceiveAddress(member.getReceiveAddress());
        try {
            //判断输入的是否是 yyyy-MM-dd
            if (member.getBirthday().split("-").length != 3) {
                return new Result(true, Global.birthday_illegal, null, null);
            }
            String str = member.getBirthday();
            str = str.substring(str.indexOf("-") + 1);
            old.setBirthDate(StringUtils.formatDate2(Calendar.getInstance().get(Calendar.YEAR) + "-" + str + " 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //会员级别有改动
        if (!member.getLeavel().equals(old.getLeavel())) {
            old.setLeavel(member.getLeavel());
            Member member_seller = memberService.findBySellerIdAndLeavel(member.getSellerId(), member.getLeavel());
            old.setDiscount(member_seller.getDiscount());
        }
        old.setGroupName(member.getGroupName());
        memberService.updateMemeber(old);
        return new Result(false, Global.do_success, old, null);
    }

    // /member/toExcel?sellerId=3&ids=23=24=32
    @RequestMapping("/toExcel")
    public Result toExcel(String sellerId,String ids) throws IOException {
        return memberService.toExcel(ids);
    }

}
