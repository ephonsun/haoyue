package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.repo.CustomeCardRepo;
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
 * Created by Lijia on 2018/4/17.
 */
@Service
public class CustomeCardService {

    @Autowired
    private CustomeCardRepo customeCardRepo;
    @Autowired
    private SellerService sellerService;

    public void save(CustomeCard customeCard) {
        customeCardRepo.save(customeCard);
    }

    public CustomeCard findOne(Integer id) {
        return customeCardRepo.findOne(id);
    }

    public void update(CustomeCard customeCard) {
        customeCardRepo.save(customeCard);
    }

    public Iterable<CustomeCard> list(Map<String, String> map, int pageNumber, int pageSize) {
        QCustomeCard customeCard =QCustomeCard.customeCard;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

                if(name.equals("sellerId")){
                    bd.and(customeCard.sellerId.eq(value));
                }
                else if(name.equals("openId")){
                    bd.and(customeCard.openId.eq(value));
                }
                else if(name.equals("type")){
                    bd.and(customeCard.type.eq(value));
                }
                else if(name.equals("active")){
                    bd.and(customeCard.active.eq(Boolean.valueOf(value)));
                }
                else if(name.equals("openIdNull")){
                    bd.and(customeCard.openId.isNull());
                }
            }
        }
        return customeCardRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,new String[]{"id"})));
    }

    public List<CustomeCard> findBySellerIdAndOpenIdAndPid(String sellerId, String openId, String pid) {
        return customeCardRepo.findBySellerIdAndOpenIdAndPid(sellerId,openId,pid);
    }


    public void flush(){
        Date date=new Date();
        //刷新数据
        customeCardRepo.flush(date);
        //访问通知
        List<CustomeCard> list= customeCardRepo.findByActiveAndUsedAndOpenIdIsNotNull(true,false);
        long expiredate=0;
        long nowdate=date.getTime();
        for(CustomeCard customeCard:list){
            //模板是否设置了提醒
            if(findRemindById(Integer.parseInt(customeCard.getPid()))==false){
                continue;
            }
            //是否已经提醒过了
            if(customeCard.getHasremind()){
                continue;
            }
            //formId格式校验
            if(StringUtils.isNullOrBlank(customeCard.getFormId())||customeCard.getFormId().contains("mock")){
                continue;
            }
            expiredate=customeCard.getExpireDate().getTime();
            //时间差小于 4 天
            if(expiredate-nowdate<3600*24*4){
                addTemplate(customeCard);
                customeCardRepo.updateHasremind(customeCard.getId());
            }
        }
    }

    private boolean findRemindById(int i) {
        return customeCardRepo.findRemindById(i);
    }


    public void getTemplate(Template template, String sellerId, String formId) {
        //获取 appId 和 secret 8bcdb74a9915b5685fa0ec37f6f25b24
        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=" + seller.getAppId() + "&secret=" + seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id =formId;
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("优惠券通知：：：" + result);
    }

    public void addTemplate(CustomeCard customeCard) {
        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(customeCard.getCardName());
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(StringUtils.formDateToStr(customeCard.getExpireDate()));
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue("卡券即将到期，请及时使用！");
        list.add(templateResponse3);

        Template template = new Template();
        template.setTemplateId(sellerService.findOne(Integer.parseInt(customeCard.getSellerId())).getCustomeCard_template());
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(customeCard.getOpenId());
        getTemplate(template, customeCard.getSellerId(), customeCard.getFormId());
    }

}
