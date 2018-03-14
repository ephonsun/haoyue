package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Member;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.OrderTotalPrice;
import com.haoyue.service.MemberService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.Global;
import com.haoyue.untils.OSSClientUtil;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @RequestMapping("/save_new")
    public Result save_new(Member member) {
        Member oldone = memberService.findBySellerIdAndLeavel(member.getSellerId(), member.getLeavel());
        if (oldone == null) {
            oldone = new Member();
            oldone.setCreateDate(new Date());
        }
        oldone.setActive(member.getActive());
        oldone.setTotal_consume(member.getTotal_consume());
        oldone.setNums(member.getNums());
        oldone.setDiscount(member.getDiscount());
        oldone.setPic(member.getPic());
        oldone.setLeavel(member.getLeavel());
        memberService.save(oldone);
        return new Result(false, Global.do_success, null, null);
    }



    //上传图片  /member/uploadPics
    @RequestMapping("/uploadPics")
    public Result uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
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
        return new Result(false, Global.do_success, stringBuffer.toString(), null);
    }


    //  查看买家会员信息 /member/findOne?sellerId=卖家ID&openId=12345
    //  /member/findOne?id=当前记录的ID&sellerId=12
    @RequestMapping("/findOne")
    public Result findOne(String sellerId, String openId,String id) {
        Member member=null;
        if(!StringUtils.isNullOrBlank(id)){
            member=memberService.findById(Integer.parseInt(id));
        }else {
            member = memberService.findByOpenIdAndSellerId(openId, sellerId);
        }

        return new Result(false, Global.do_success, member, null);
    }

    // 会员列表 /member/list?pageSize=当前页(从0开始)&groupname=分组名称&wxname=微信名称&lev=会员级别
    //  &expense_from=交易额开始&expense_to=交易额结束&nums_from=交易笔数开始&nums_to=交易笔数结束
    //  &datefrom=上次交易时间开始&dateto=上次交易时间结束&sex=性别&birthfrom=生日开始&birthto=生日结束
    //  &discount=折扣
    @RequestMapping("/list")
    public Result list_new(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Member> iterable = memberService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, null, null);
    }

}
