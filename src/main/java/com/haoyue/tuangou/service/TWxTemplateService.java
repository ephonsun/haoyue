package com.haoyue.tuangou.service;


import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.repo.TUserBuyRepo;
import com.haoyue.tuangou.repo.TWxTemplateRepo;
import com.haoyue.tuangou.utils.CommonUtil;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.wxpay.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/17.
 */
@Service
public class TWxTemplateService {

    @Autowired
    private TWxTemplateRepo wxTemplateRepo;
    @Autowired
    private TUserBuyRepo userBuyRepo;


    public void save(TWxTemplate wxTemplate) {
        wxTemplateRepo.save(wxTemplate);
    }


    //自动刷新 active
    public void autoFlush(){
        Date date=new Date();
        wxTemplateRepo.autoFlush(date);
        inform();
    }

    //访问通知
    public void inform(){
        // 每日 10点、20点 发送访问通知
        Date date=new Date();
        if (date.getHours()==10||date.getHours()==20){
            List<String> openIds= wxTemplateRepo.findDistinctOpenId();
            List<TWxTemplate> list=new ArrayList<>();
            for (String openid:openIds){
                list=wxTemplateRepo.findByActiveAndOpneId(openid);
                if (list==null||list.size()==0){
                    continue;
                }
                TWxTemplate wxTemplate=list.get(0);
                if (wxTemplate.getFormId().contains("the formId is a mock one")){
                    continue;
                }
                addTemplate(wxTemplate);
                //更新 active
                wxTemplateRepo.updateActive(wxTemplate.getId());
            }
        }
    }

    //   访问通知
    public void addTemplate(TWxTemplate wxTemplate){
        List<TemplateResponse> list=new ArrayList<>();

        TemplateResponse templateResponse1=new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        TUserBuy userBuy=userBuyRepo.findBySaleIdAndOpenId(wxTemplate.getSaleId(),wxTemplate.getOpenId());
        if (StringUtils.isNullOrBlank(userBuy.getWxname())||userBuy.getWxname().contains("rdgztest")){
            return;
        }else {
            templateResponse1.setValue(userBuy.getWxname());
        }
        list.add(templateResponse1);

        TemplateResponse templateResponse2=new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue("您已很久没来团实惠啦，快来挑选精品好物吧....");
        list.add(templateResponse2);

        String page="pages/index/index";

        Template template=new Template();
        template.setTemplateId("BjAcNJj6PPsfWhbn_chabdetsU5mAK1-M_UvDU083Dc");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage(page);
        template.setToUser(wxTemplate.getOpenId());
        getTemplate(template,wxTemplate.getFormId(),wxTemplate.getSaleId());
    }


    public void getTemplate(Template template,String formId,String saleId){
        //模板信息通知用户
        //获取 access_token
        String access_token= TGlobal.access_tokens.get(saleId);
        if (StringUtils.isNullOrBlank(access_token)) {
            String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
            String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
            access_token = HttpRequest.sendPost(access_token_url, param1);
            access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
            TGlobal.access_tokens.put(saleId,access_token);
        }
        //发送模板信息
        String form_id="";
        if (StringUtils.isNullOrBlank(formId)) {
            form_id= TGlobal.tuan_package_map.get(template.getToUser());
        }else {
            form_id=formId;
        }
        template.setForm_id(form_id);
        String url="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token+"&form_id="+form_id;
        String result= CommonUtil.httpRequest(url,"POST",template.toJSON());
        System.out.println("访问通知结果："+result);
    }


}
