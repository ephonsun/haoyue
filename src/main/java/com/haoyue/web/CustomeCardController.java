package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.CustomeCard;
import com.haoyue.service.CustomeCardService;
import com.haoyue.untils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/4/17.
 */
@RestController
@RequestMapping("/customecard")
public class CustomeCardController {


    @Autowired
    private CustomeCardService customeCardService;

//      /customecard/save?sellerId=3&cardName=优惠券名称&totalNums=总发放量&type=优惠形式(金额 0 折扣 1)
//      &typeValue=优惠额度picSmall=小图标&requireMent=使用门槛(满 0-n 元可使用)&maxGets=单人最多可领取X张
//      &remind=true/false(到期4天前提醒)&share=true/false(是否可以分享)
//       &whereuse=可使用商品(0 全部商品 1 指定商品 2 仅原价购买商品)&comments=备注说明&phone=客服电话
//       &expiretype=有效期(0 固定日期 1 从领取当日开始计算  2 从领取次日开始计算)
//       &[ 如果 expiretype=0 的话: beginDateStr=开始日期(格式 2017-9-19 16:28:25)&expireDateStr=结束日期(格式 同上)]
//       &[  如果 expiretype=1/2 的话: expiredays=有效期几日]
    @RequestMapping("/save")
    public Result save(CustomeCard customeCard) throws ParseException {
        customeCard.setCreateDate(new Date());
        if(customeCard.getExpiretype().equals("0")){
            customeCard.setBeginDate(StringUtils.formatDate2(customeCard.getBeginDateStr()));
            customeCard.setExpireDate(StringUtils.formatDate2(customeCard.getExpireDateStr()));
        }
        customeCardService.save(customeCard);
        return new Result(false, Global.do_success, customeCard, null);
    }

    // 卖家关闭优惠券  /customecard/del?id=优惠券Id&sellerId=3
    @RequestMapping("/del")
    public Result del(Integer id, String sellerId, String openId) {
        CustomeCard customeCard = customeCardService.findOne(id);
        customeCard.setActive(false);
        customeCardService.update(customeCard);
        return new Result(false, Global.do_success, null, null);
    }


//      卖家设置的正在生效的优惠券列表  /customecard/list?sellerId=3&openIdNull=yes&active=true
//      卖家设置的已经失效了的优惠券列表  /customecard/list?sellerId=3&openIdNull=yes&active=false
//      查看指定买家的优惠券 /customecard/list?sellerId=3&openId=123(&active=false（已经失效） used=true（已经使用） )
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<CustomeCard> iterable = customeCardService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }


    //上传图片  /customecard/uploadPics?multipartFiles=图片文件
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
                //stringBuffer.append(",");
            }
        }
        return new UploadSuccessResult(stringBuffer.toString());
    }

    //  /customecard/gets?sellerId=3&openId=31&formId=用于模板消息&pid=领取的优惠券模板ID
    @RequestMapping("/gets")
    public Result gets(CustomeCard customeCard) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        CustomeCard parent = customeCardService.findOne(Integer.parseInt(customeCard.getPid()));

        //判断当前用户有没有超过领取优惠券的最大数量
        int maxGets = parent.getMaxGets();
        List<CustomeCard> list = customeCardService.findBySellerIdAndOpenIdAndPid(customeCard.getSellerId(), customeCard.getOpenId(), customeCard.getPid());
        if (list != null && list.size() >= maxGets) {
            return new Result(false, Global.already_MaxGets + maxGets, null, null);
        }
        //判断当前母体优惠券有效期模式是否为0
        if (parent.getExpiretype().equals("0")) {
            //判断当前母体优惠券是否过期
            if (parent.getExpireDate().before(calendar.getTime())) {
                return new Result(false, Global.alreay_expire, null, null);
            }
        }
        //判断卖家设置的优惠券 active
        if (parent.getActive() == false) {
            return new Result(false, Global.alreay_expire, null, null);
        }
        //判断当前母体优惠券是否领取完毕
        if (parent.getGotNums() >= parent.getTotalNums()) {
            return new Result(false, Global.already_got_all, null, null);
        }

        //开始领取
        customeCard.setCreateDate(calendar.getTime());
        customeCard.setActive(true);
        customeCard.setCardName(parent.getCardName());
        customeCard.setComments(parent.getComments());
        customeCard.setGotNums(1);
        customeCard.setPicSmall(parent.getPicSmall());
        customeCard.setRemind(parent.getRemind());
        customeCard.setRequireMent(parent.getRequireMent());
        customeCard.setShare(parent.getShare());
        customeCard.setType(parent.getType());
        customeCard.setTypeValue(parent.getTypeValue());
        customeCard.setWhereuse(parent.getWhereuse());
        customeCard.setPhone(parent.getPhone());
        //优惠券有效期
        if (parent.getExpiretype().equals("0")) {
            customeCard.setBeginDate(StringUtils.formatDate2(parent.getBeginDateStr()));
            customeCard.setExpireDate(StringUtils.formatDate2(parent.getExpireDateStr()));
        } else if (parent.getExpiretype().equals("1")) {
            customeCard.setBeginDate(calendar.getTime());
            calendar.add(Calendar.DATE, parent.getExpiredays());
            customeCard.setExpireDate(calendar.getTime());
        } else if (parent.getExpiretype().equals("2")) {
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            customeCard.setBeginDate(calendar.getTime());
            calendar.add(Calendar.DATE, parent.getExpiredays());
            customeCard.setExpireDate(calendar.getTime());
        }
        customeCardService.save(customeCard);

        //更新已领取量
        parent.setGotNums(parent.getGotNums() + 1);
        customeCardService.update(parent);

        return new Result(false, Global.do_success, customeCard, null);
    }

    //  /customecard/useCard?openId=31&id=当前优惠券Id
    @RequestMapping("/useCard")
    public Result useCard(Integer id, String openId) {
        CustomeCard customeCard = customeCardService.findOne(id);
        if (!customeCard.getOpenId().equals(openId)) {
            return new Result(false, Global.have_no_right, null, null);
        }
        if (customeCard.getExpireDate().before(new Date())) {
            return new Result(false, Global.alreay_expire, null, null);
        }
        if (customeCard.getUsed()) {
            return new Result(false, Global.already_used, null, null);
        }
        customeCard.setUsed(true);
        customeCard.setActive(false);
        customeCardService.update(customeCard);
        //更新母体已经使用量
        CustomeCard parent = customeCardService.findOne(Integer.parseInt(customeCard.getPid()));
        parent.setGotUsed(parent.getGotUsed() + 1);
        customeCardService.update(parent);
        return new Result(false, Global.do_success, customeCard, null);
    }


}
