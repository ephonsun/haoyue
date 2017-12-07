package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.service.TCouponService;
import com.haoyue.tuangou.service.TRedPacketService;
import com.haoyue.tuangou.service.TUserSaleService;
import com.haoyue.tuangou.utils.CommonUtil;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import com.haoyue.tuangou.wxpay.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/6.
 */
@RestController
@RequestMapping("/tuan/redpacket")
public class TRedPacketController {

    @Autowired
    private TRedPacketService redPacketService;

    @Autowired
    private TCouponService couponService;

    @Autowired
    private TUserSaleService userSaleService;


    //领取红包，自己是房主  拆红包 自己不是房主
    //  领取红包 /tuan/redpacket/save?saleId=12&openId=12&wxname=微信名&wxpic=头像&formId=模板ID&formId2=模板ID&isowener=true
    //  拆红包  /tuan/redpacket/save?saleId=12&openId=12&wxname=微信名&wxpic=头像&formId=模板ID&formId2=模板ID&groupCode=房主红包的单号&isowener=false&ownerOpenId=房主的openId
    @RequestMapping("/save")
    public TResult save(TRedPacket redPacket, String ownerOpenId) {
        TUserSale userSale=userSaleService.findOneById(Integer.parseInt(redPacket.getSaleId()));
        if (userSale.getRedpacket()==false){
            return new TResult(true, TGlobal.redpacket_not_open, null);
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        redPacket.setCreateDate(date);
        redPacket.setEndDate(calendar.getTime());
        if (StringUtils.isNullOrBlank(redPacket.getGroupCode())) {
            redPacket.setGroupCode(date.getTime() + "");
        }
        redPacket.setMoney(randomMoney(redPacket.getGroupCode()));
        int sum = 0;
        //查看用户是否已经帮房主拆过红包
        synchronized (TGlobal.object6) {
            if (redPacket.getIsowener() == false) {
                if (StringUtils.isNullOrBlank(ownerOpenId)) {
                    List<TRedPacket> list = redPacketService.findByOpenIdAndIsOwener(ownerOpenId, redPacket.getSaleId());
                    sum = list.size();
                    //拆过 返回提示
                    for (TRedPacket tRedPacket : list) {
                        if (tRedPacket.getOpener().contains(redPacket.getOpenId())) {
                            return new TResult(true, TGlobal.haved_open, null);
                        }
                    }
                    //未拆过 更新房主红包的opener
                    TRedPacket tRedPacket = redPacketService.findByGroupCodeAndIsOwener(redPacket.getGroupCode());
                    if (tRedPacket.getIsover() == true || date.after(tRedPacket.getEndDate())) {
                        return new TResult(true, TGlobal.redpacket_is_end, null);
                    }
                    tRedPacket.setOpener(tRedPacket.getOpener() + "," + redPacket.getOpenId());
                    redPacketService.save(tRedPacket);
                }
            }
            redPacketService.save(redPacket);
            //如果红包人数达到4个人
            if (sum == 3) {
                //转优惠券
                List<TCoupon> coupons= tocoupon(redPacket.getGroupCode());
                //参加者微信名
                List<String> names= redPacketService.findWxnameByGroupCode(redPacket.getGroupCode());
                String name="";
                for (String str:names){
                    if (!StringUtils.isNullOrBlank(str)){
                        name=name+","+str;
                    }
                }
                name=name.substring(1);
                //更新红包状态
                redPacketService.updateIsoverByGroupCode(redPacket.getGroupCode());
                //模板信息
                for (TCoupon coupon:coupons) {
                    addTemplate(coupon,name);
                }
            }
        }
        return new TResult(true, TGlobal.do_success, null);
    }

    // 指定用户的红包列表 /tuan/redpacket/list?openId=123&saleId=123&[isover=true/false]
    @RequestMapping("/list")
    public TResult list(String openId,String saleId,String isover){
        List<TRedPacket> list=new ArrayList<>();
        if (StringUtils.isNullOrBlank(isover)) {
             list = redPacketService.findBySaleIdAndOpenId(saleId, openId);
        }else {
            list = redPacketService.findBySaleIdAndOpenIdAndIsover(saleId, openId,Boolean.valueOf(isover));
        }
        return new TResult(true, TGlobal.do_success, list);
    }


    //  /tuan/redpacket/list?groupcode=1221
    @RequestMapping("/code")
    public TResult findByGroupCode(String groupcode){
        List<TRedPacket> redPackets= redPacketService.findByGroupCode(groupcode);
        return new TResult(true, TGlobal.do_success, redPackets);
    }


    // 转优惠券
    public List<TCoupon> tocoupon(String groupcode){
        List<TRedPacket> redPackets= redPacketService.findByGroupCode(groupcode);
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,4);
        List<TCoupon> coupons=new ArrayList<>();
        for (TRedPacket redPacket:redPackets){
            TCoupon coupon=new TCoupon();
            coupon.setCreateDate(date);
            coupon.setMoney(redPacket.getMoney());
            coupon.setActive(true);
            coupon.setEndDate(calendar.getTime());
            coupon.setIsuse(false);
            coupon.setOpenId(redPacket.getOpenId());
            coupon.setSaleId(redPacket.getSaleId());
            coupon.setWxname(redPacket.getWxname());
            coupon.setFormId(redPacket.getFormId());
            couponService.save(coupon);
            coupons.add(coupon);
        }
        return coupons;
    }


    // 红包随机金额
    public int randomMoney(String groupcode) {
        int a[] = {5, 10, 15, 20, 25};
        List<TRedPacket> list = redPacketService.findByGroupCode(groupcode);
        int index = (int) Math.ceil(Math.random() * 5);
        int money = a[index];
        int sum = 0;
        if (list != null) {
            for (TRedPacket redPacket : list) {
                sum += redPacket.getMoney();
            }
            // 2个人
            if (list.size() == 1) {
                while (sum + money > 40) {
                    index = (int) Math.floor(Math.random() * 5);
                    money = a[index];
                }
            }
            // 3个人
            if (list.size() == 2) {
                while (sum + money > 45) {
                    index = (int) Math.floor(Math.random() * 5);
                    money = a[index];
                }
            }
            // 4 个人
            if (list.size() == 3) {
                while (sum + money != 50) {
                    index = (int) Math.floor(Math.random() * 5);
                    money = a[index];
                }
            }
            return money;
        }
        return money;
    }

    //4人拆红包成功通知
    public void addTemplate(TCoupon coupon,String name){
        List<TemplateResponse> list=new ArrayList<>();
        TemplateResponse templateResponse1=new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(String.valueOf(coupon.getMoney())+"元优惠券");
        list.add(templateResponse1);

        TemplateResponse templateResponse2=new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(coupon.getCreateDate().toLocaleString());
        list.add(templateResponse2);

        TemplateResponse templateResponse3=new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        if (name.contains(coupon.getWxname())){
            name.replace(coupon.getWxname(),"");
        }
        templateResponse3.setValue("恭喜你和:"+name+" 瓜分红包成功，系统奖励优惠券已到账");
        list.add(templateResponse3);


        Template template=new Template();
        template.setTemplateId("iTNVkRceX_5Ze5rNjLvP46hBz-rM_xbHwd1sPFKmR6s");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        // todo  定向到优惠券页面
        template.setPage("pages/index/index");
        template.setToUser(coupon.getOpenId());
        getTemplate(template,coupon.getFormId());
    }

    public void getTemplate(Template template,String formId){
        //模板信息通知用户
        //获取 access_token
        String access_token_url="https://api.weixin.qq.com/cgi-bin/token";
        String param1="grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
        String access_token= HttpRequest.sendPost(access_token_url,param1);
        access_token=access_token.substring(access_token.indexOf(":")+2,access_token.indexOf(",")-1);
        //发送模板信息
        template.setForm_id(formId);
        String url="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token+"&form_id="+formId;
        String result= CommonUtil.httpRequest(url,"POST",template.toJSON());
    }


}
