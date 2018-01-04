package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.repo.DictionaryRepo;
import com.haoyue.repo.SellerRepo;
import com.haoyue.repo.VisitorsRepo;
import com.haoyue.untils.*;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class DictionaryService {

    @Autowired
    private DictionaryRepo dictionaryRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private VisitorsService visitorsService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WxTemplateService wxTemplateService;
    @Autowired
    private CustomerService customerService;

    public Dictionary findByTokenAndName(String token, String name) {
        return null;
    }

    public Result save(Dictionary dictionary, String token) {
        if (dictionary.getId() != null) {
            if (dictionary.getSellerId() != Integer.parseInt(token)) {
                return new Result(true, Global.have_no_right, token);
            }
        } else {
            dictionary.setSellerId(Integer.parseInt(token));
            dictionary.setCreateDate(new Date());
            dictionary.setBuyers(0);
            dictionary.setViews(0);
            dictionary.setVisitors(0);
            dictionary.setTurnover(0.00);
        }
        return new Result(dictionaryRepo.save(dictionary), token);
    }

    public Result findBySellerId(int sid) {
        List<Dictionary> list = dictionaryRepo.findBySellerId(sid);
        if (list.size() == 0) {
            return new Result(false, Global.first_login, null, sid + "");
        }
        return new Result(list, sid + "");
    }

    public Iterable<Dictionary> findBySellerId2(int sid, Integer pageNumber, Integer pageSize) {
        QDictionary dictionary = QDictionary.dictionary;
        BooleanBuilder bd = new BooleanBuilder();
        Date from = new Date();
        Date to = new Date();
        int month = from.getMonth() - 1;
        if (month == -1) {
            month = 11;
            from.setYear(to.getYear() - 1);
        }
        from.setMonth(month);
        month++;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            from.setDate(24);
        } else {
            from.setDate(23);
        }
        bd.and(dictionary.createDate.between(from, to));
        bd.and(dictionary.sellerId.eq(sid));
        bd.and(dictionary.productId.isNull());
        return dictionaryRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));

    }

    public Dictionary findByProductId(Integer pid) {
        return dictionaryRepo.findByProductId(pid);
    }

    public void addEachDay() {
        //访问通知
        auto_inform();
        //默认收货
        auto_receive();
        //每天向dictionary表注入当日新的数据
        Dictionary dictionery = dictionaryRepo.findLast();
        Date date = StringUtils.getYMD(new Date());
        if ((!dictionery.getCreateDate().equals(date))) {
            List<Integer> ids = sellerRepo.findIds();
            for (Integer id : ids) {
                Dictionary dictionary = new Dictionary();
                dictionary.setTurnover(0.0);
                dictionary.setVisitors(0);
                dictionary.setViews(0);
                dictionary.setBuyers(0);
                dictionary.setSellerId(id);
                dictionary.setCreateDate(date);
                dictionaryRepo.save(dictionary);
            }
            //每日清空 visitors 表
            visitorsService.delAll();
            //判断年份是否改变,新的一年刷新所有会员信息
//            if (dictionery.getCreateDate().getYear()!=date.getYear()){
//                flushMembers();
//            }
            //清空当日生成的excel文件
            //clear_excel();
        }
    }

    //访问通知
    public void auto_inform() {
        wxTemplateService.updateActive();
        List<String> openids = wxTemplateService.findActive();
        for (String openid : openids) {
            List<WxTemplate> wxTemplates=wxTemplateService.findByOpenId(openid);
            for (WxTemplate wxTemplate:wxTemplates){
                if (wxTemplate.getFormId().contains("mock")){
                    wxTemplate.setActive(false);
                    wxTemplateService.save(wxTemplate);
                    continue;
                }
                addTemplate(customerService.findByOpenId(wxTemplate.getOpenId(),wxTemplate.getSellerId()).getWxname(),wxTemplate.getFormId(),wxTemplate.getSellerId());
                wxTemplate.setActive(false);
                wxTemplateService.save(wxTemplate);
                break;
            }
        }
    }

    public void auto_receive() {
        //待收货
        List<Order> orders = orderService.findUnDone(Global.order_send);
        Date now_date = new Date();
        Date old_date = null;
        for (Order order : orders) {
            Deliver deliver = order.getDeliver();
            if (deliver == null) {
                continue;
            }
            old_date = deliver.getCreateDate();
            //判断距离发货日期的时间差
            if ((now_date.getTime() - old_date.getTime()) > 1000 * 60 * 60 * 24 * 15) {
                //order.setState(Global.order_finsh);
                orderService.autoDone(order.getId());
            }
        }
    }

    public void clear_excel() {
        //清空生成的excel文件
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        if (Global.excel_urls.size() != 0) {
            for (String s : Global.excel_urls) {
                ossClientUtil.delete(s);
            }
            //清空Global.excel_urls
            Global.excel_urls.clear();
        }
    }

    public void flushMembers() {
        //删除普通会员
        memberService.delVip();
        //高级会员和至尊会员降一级
        List<Member> memberList = memberService.findByOpenIdIsNotNull();
        String discount_1 = "";
        String discount_2 = "";
        String total_consume1 = "";
        String total_consume2 = "";
        List<Member> memberList1 = new ArrayList<>();
        for (Member member : memberList) {
            //找到买家对应的卖家会员模板信息
            memberList1 = memberService.findBySellerIdAndOpenIdIsNull(member.getSellerId());
            for (Member member1 : memberList1) {
                if (member1.getLeavel().equals("lev1")) {
                    discount_1 = member1.getDiscount();
                    total_consume1 = member1.getTotal_consume();
                }
                if (member1.getLeavel().equals("lev2")) {
                    discount_2 = member1.getDiscount();
                    total_consume2 = member1.getTotal_consume();
                }
            }
            if (member.getLeavel().equals("lev2")) {
                member.setLeavel("lev1");
                member.setDiscount(discount_1);
                member.setTotal_consume(total_consume1);
            }
            if (member.getLeavel().equals("lev3")) {
                member.setLeavel("lev2");
                member.setDiscount(discount_2);
                member.setTotal_consume(total_consume2);
            }
            memberService.save(member);
        }
    }

    public void addEachDay2() {

        List<Integer> ids = sellerRepo.findIds();
        Date date = StringUtils.getYMD(new Date());
        // addViews和addVisitors可导致并发执行的情况
        synchronized (Global.object3) {
            for (Integer id : ids) {
                //判断新添加数据存不存在
                if (dictionaryRepo.findBySellerIdAndCreateDateAndProductIdIsNull(id, date) != null) {
                    continue;
                }
                Dictionary dictionary = new Dictionary();
                dictionary.setTurnover(0.0);
                dictionary.setVisitors(0);
                dictionary.setViews(0);
                dictionary.setBuyers(0);
                dictionary.setSellerId(id);
                dictionary.setCreateDate(date);
                dictionaryRepo.save(dictionary);
            }
        }
        //每日清空 visitors 表
        visitorsService.delAll();
    }

    public Result del(Integer id) {
        dictionaryRepo.delete(id);
        return new Result(false, "", null);
    }

    public Dictionary findByDateAndSellerId(Date date, Integer sellerId) {
        return dictionaryRepo.findBySellerIdAndCreateDateAndProductIdIsNull(sellerId, StringUtils.getYMD(date));

    }

    public void update(Dictionary dictionary) {
        dictionaryRepo.save(dictionary);
    }

    public void addProduct(Products products) {
        Dictionary dictionary = new Dictionary();
        dictionary.setProductId(products.getId());
        dictionary.setTurnover(0.0);
        dictionary.setVisitors(0);
        dictionary.setBuyers(0);
        dictionary.setViews(0);
        dictionary.setCreateDate(new Date());
        dictionary.setSellerId(products.getSellerId());
        dictionaryRepo.save(dictionary);
    }

    public void getTemplate(Template template, String formId) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxe46b9aa1b768e5fe&secret=8bcdb74a9915b5685fa0ec37f6f25b24";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);

        //发送模板信息
        template.setForm_id(formId);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + formId;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("高级版访问通知："+result);
    }

    public void addTemplate(String wxname, String formId, String openId) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(wxname);
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue("关注女装羊绒大衣，春款超超值购即将开始！");
        list.add(templateResponse2);

        Template template = new Template();
        template.setTemplateId("Foc6pkGtA2FZON2_5nXJxCmFvfIdKWAv5oj6REd_3w4");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(openId);
        getTemplate(template, formId);
    }
}
