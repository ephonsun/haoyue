package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.repo.RechargeRecordRepo;
import com.haoyue.untils.CommonUtil;
import com.haoyue.untils.Global;
import com.haoyue.untils.HttpRequest;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/6/4.
 */

@Service
public class RechargeRecordService {

    @Autowired
    private RechargeRecordRepo rechargeRecordRepo;
    @Autowired
    private SellerService sellerService;

    public void save(RechargeRecord rechargeRecord) {
        rechargeRecordRepo.save(rechargeRecord);
    }

    public RechargeRecord findByOrderCode(String out_trade_no) {
        return rechargeRecordRepo.findByOrdercode(out_trade_no);
    }


    public void getTemplate(Template template, RechargeRecord rechargeRecord) {
        //获取 appId 和 secret 8bcdb74a9915b5685fa0ec37f6f25b24
        Seller seller = sellerService.findOne(Integer.parseInt(rechargeRecord.getSellerId()));
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=" + seller.getAppId() + "&secret=" + seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id = Global.package_map.get(rechargeRecord.getOrdercode());
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("充值付款通知：：：" + result);
        //刷新 Global.package_map
        Global.package_map.remove(rechargeRecord.getOrdercode());
    }

    public void addTemplate(RechargeRecord rechargeRecord) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue("禾才充值卡");
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(rechargeRecord.getWxname());
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(rechargeRecord.getTotalMoney() + "");
        list.add(templateResponse3);

        TemplateResponse templateResponse4 = new TemplateResponse();
        templateResponse4.setColor("#000000");
        templateResponse4.setName("keyword4");
        templateResponse4.setValue("微信支付");
        list.add(templateResponse4);

        TemplateResponse templateResponse5 = new TemplateResponse();
        templateResponse5.setColor("#000000");
        templateResponse5.setName("keyword5");
        String date = StringUtils.formDateToStr(new Date());
        templateResponse5.setValue(date);
        list.add(templateResponse5);

        Template template = new Template();
        template.setTemplateId(sellerService.findOne(Integer.parseInt(rechargeRecord.getSellerId())).getPaysuccess_template());
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(rechargeRecord.getOpenId());
        getTemplate(template, rechargeRecord);
    }


    public void change(RechargeRecord rechargeRecord) {
        rechargeRecord.setState("已支付");
        rechargeRecordRepo.save(rechargeRecord);
        addTemplate(rechargeRecord);
    }

    public Iterable<RechargeRecord> list(Map<String, String> map, int pageNumber, int pageSize) {

        QRechargeRecord rechargeRecord = QRechargeRecord.rechargeRecord;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(rechargeRecord.state.eq("已支付"));
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (!StringUtils.isNullOrBlank(value)) {
                if(key.equals("sellerId")){
                    bd.and(rechargeRecord.sellerId.eq(value));
                }
                if(key.equals("openId")){
                    bd.and(rechargeRecord.openId.eq(value));
                }

            }

        }

        return rechargeRecordRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));

    }
}
