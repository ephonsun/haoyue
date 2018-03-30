package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.pojo.Dictionary;
import com.haoyue.repo.DictionaryRepo;
import com.haoyue.repo.SellerRepo;
import com.haoyue.repo.VisitorsRepo;
import com.haoyue.tuangou.utils.*;
import com.haoyue.untils.*;
import com.haoyue.untils.CommonUtil;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    private ProductsService productsService;
    @Autowired
    private PtypeNamesService ptypeNamesService;
    @Autowired
    private SellerService sellerService;

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

        if (true) {
            System.out.println("高级版--访问通知");
            //首先更新一下表数据
            wxTemplateService.updateActive();
            //取出 distinct 且 active=true 的 openId
            List<String> openids = wxTemplateService.findActive();
            Date date = new Date();
            for (String openid : openids) {
                //方便自己观察模板信息发送情况  ook0P0VO6YbmFq37iAazBWLDAnsg
                if (date.getHours() == 12 || openid.equals("ook0P0VO6YbmFq37iAazBWLDAnsg")) {
                    //过滤
                    if (openid == null || openid.equals("undefined")) {
                        continue;
                    }
                    //找出指定openId对应的所有active=true 的 WxTemplate
                    List<WxTemplate> wxTemplates = wxTemplateService.findByOpenId(openid);
                    //过滤掉未在customer中注册的open_id
                    Customer customer = customerService.findByOpenId(openid, wxTemplates.get(0).getSellerId());
                    if (customer == null) {
                        continue;
                    }
                    for (WxTemplate wxTemplate : wxTemplates) {
                        if (StringUtils.isNullOrBlank(wxTemplate.getButtonName())) {
                            //过滤formId
                            if (wxTemplate.getFormId().contains("mock") || wxTemplate.getFormId().contains("undefined")) {
                                wxTemplate.setActive(false);
                                wxTemplateService.save(wxTemplate);
                                continue;
                            }
                            Seller seller = sellerService.findOne(Integer.parseInt(wxTemplate.getSellerId()));
                            addTemplate(customerService.findByOpenId(wxTemplate.getOpenId(), wxTemplate.getSellerId()).getWxname(), wxTemplate.getFormId(), wxTemplate.getOpenId(), seller.getService_template_msg(), "pages/index/index", seller.getSellerId(), seller.getService_template());
                            wxTemplate.setActive(false);
                            wxTemplateService.save(wxTemplate);
                            break;
                        }
                    }
                }
            }
        }

    }

    //秒杀通知
    public void timeKillInform() {
        //首先更新一下表数据
        wxTemplateService.updateActive();
        //取出 distinct 且 active=true buttonName=秒杀 的 openId
        String str = "秒杀";
        List<String> openids = wxTemplateService.findActiveAndButtonName(str);
        for (String openid : openids) {
            //过滤
            if (openid == null || openid.equals("undefined")) {
                continue;
            }
            //找出指定openId对应的所有active=true 的 WxTemplate
            List<WxTemplate> wxTemplates = wxTemplateService.findByOpenId(openid);
            //过滤掉未在customer中注册的open_id
            Customer customer = customerService.findByOpenId(openid, wxTemplates.get(0).getSellerId());
            if (customer == null) {
                continue;
            }
            for (WxTemplate wxTemplate : wxTemplates) {
                //过滤formId
                if (wxTemplate.getFormId().contains("mock") || wxTemplate.getFormId().contains("undefined")) {
                    wxTemplate.setActive(false);
                    wxTemplateService.save(wxTemplate);
                    continue;
                }
                //获取当前wxTemplate中的sellerId
                String sellerId = wxTemplate.getSellerId();
                if (StringUtils.isNullOrBlank(Global.miaosha_map.get(sellerId))) {
                    //  Global.miaosha_map 中没有当前key=sellerId信息
                    Map<String, String> map = new HashMap<>();
                    map.put("token", sellerId);
                    map.put("active", "true");
                    map.put("secondKillStart", "yes");
                    map.put("secondKillEnd", "yes");
                    Iterable<Products> iterable = productsService.list(map);
                    //存在在售秒杀商品
                    if (iterable.iterator().hasNext()) {
                        //  Global.miaosha_map 中加入当前key=sellerId信息 yes
                        Global.miaosha_map.put(sellerId, "yes");
                        String pagePath = getPagePath(sellerId, "秒杀");
                        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
                        addTemplate(customer.getWxname(), wxTemplate.getFormId(), wxTemplate.getOpenId(), Global.wxtemplate_msg3, pagePath, seller.getSellerId(), seller.getService_template());
                        wxTemplate.setActive(false);
                        wxTemplateService.save(wxTemplate);
                        break;
                    } else {
                        //  Global.miaosha_map 中加入当前key=sellerId信息 no
                        Global.miaosha_map.put(sellerId, "no");
                        break;
                    }
                } else {
                    //  Global.miaosha_map 中获取当前key=sellerId信息
                    if (Global.miaosha_map.get(sellerId).equals("yes")) {
                        // key=sellerId value=yes
                        String pagePath = getPagePath(sellerId, "秒杀");
                        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
                        addTemplate(customer.getWxname(), wxTemplate.getFormId(), wxTemplate.getOpenId(), Global.wxtemplate_msg3, pagePath, seller.getSellerId(), seller.getService_template());
                        wxTemplate.setActive(false);
                        wxTemplateService.save(wxTemplate);
                        break;
                    }
                }
            }
        }
    }

    //预售通知
    public void advanceSale() {
        //首先更新一下表数据
        wxTemplateService.updateActive();
        //取出 distinct 且 active=true buttonName=秒杀 的 openId
        String str = "预售";
        List<String> openids = wxTemplateService.findActiveAndButtonName(str);
        for (String openid : openids) {
            //过滤
            if (openid == null || openid.equals("undefined")) {
                continue;
            }
            //找出指定openId对应的所有active=true 的 WxTemplate
            List<WxTemplate> wxTemplates = wxTemplateService.findByOpenId(openid);
            //过滤掉未在customer中注册的open_id
            Customer customer = customerService.findByOpenId(openid, wxTemplates.get(0).getSellerId());
            if (customer == null) {
                continue;
            }
            for (WxTemplate wxTemplate : wxTemplates) {
                //过滤formId
                if (wxTemplate.getFormId().contains("mock") || wxTemplate.getFormId().contains("undefined")) {
                    wxTemplate.setActive(false);
                    wxTemplateService.save(wxTemplate);
                    continue;
                }
                //获取当前wxTemplate中的sellerId
                String sellerId = wxTemplate.getSellerId();
                if (StringUtils.isNullOrBlank(Global.yushou_map.get(sellerId))) {
                    //  Global.yushou_map 中没有当前key=sellerId信息
                    Map<String, String> map = new HashMap<>();
                    map.put("token", sellerId);
                    map.put("active", "true");
                    map.put("showdate_after", "yes");
                    Iterable<Products> iterable = productsService.list(map);
                    Iterator<Products> iterator = iterable.iterator();
                    boolean flag = false;
                    while (iterator.hasNext()) {
                        //即将上架 < 1h
                        Products products = iterator.next();
                        if (products.getShowDate().getTime() - new Date().getTime() < 3600000) {
                            flag = true;
                            System.out.println("yuhsou flag:" + flag);
                        }
                    }
                    //存在预售商品
                    if (flag) {
                        //  Global.yushou_map 中加入当前key=sellerId信息 yes
                        Global.yushou_map.put(sellerId, "yes");
                        String pagePath = getPagePath(sellerId, "预售");
                        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
                        addTemplate(customer.getWxname(), wxTemplate.getFormId(), wxTemplate.getOpenId(), Global.wxtemplate_msg2, pagePath, seller.getSellerId(), seller.getService_template());
                        wxTemplate.setActive(false);
                        wxTemplateService.save(wxTemplate);
                        break;
                    } else {
                        //  Global.yushou_map 中加入当前key=sellerId信息 no
                        Global.yushou_map.put(sellerId, "no");
                        break;
                    }
                } else {
                    //  Global.yushou_map 中获取当前key=sellerId信息
                    if (Global.yushou_map.get(sellerId).equals("yes")) {
                        // key=sellerId value=yes
                        String pagePath = getPagePath(sellerId, "预售");
                        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
                        addTemplate(customer.getWxname(), wxTemplate.getFormId(), wxTemplate.getOpenId(), Global.wxtemplate_msg2, pagePath, seller.getSellerId(), seller.getService_template());
                        wxTemplate.setActive(false);
                        wxTemplateService.save(wxTemplate);
                        break;
                    }
                }
            }
        }
    }


    //官网小程序访问通知
    public void website_inform() {
        //首先更新一下表数据
        wxTemplateService.updateActive();
        List<WxTemplate> list = wxTemplateService.findByButtonName("官网");
        String pagePath = "pages/index/index";
        String templateId = "_vURY92OKlK5MiN-zZydqz0Nx1tp8-lZTe-e0pPen6Q";
        String message = "皓月小程序制作访问通知(测试)";
        for (WxTemplate wxTemplate : list) {
            Customer customer = customerService.findByOpenId(wxTemplate.getOpenId(), wxTemplate.getSellerId());
            addTemplate(customer.getWxname(), wxTemplate.getFormId(), customer.getOpenId(), message, pagePath, Integer.parseInt(wxTemplate.getSellerId()), templateId);
        }
    }

    public String getPagePath(String sellerId, String key) {
        String pagePath = "";
        PtypeNames parents = ptypeNamesService.findBySellerId(sellerId);
        String ptypename = parents.getPtypenames();
//        if (parents != null) {
//            ptypename = parents.getPtypenames();
//        } else {
//            for (PtypeNames p : list) {
//                if (!StringUtils.isNullOrBlank(p.getPtypename())) {
//                    ptypename += "," + p.getPtypename();
//                }
//            }
//        }
        String[] ptypenames = ptypename.split(",");
        for (int i = 0; i < ptypenames.length; i++) {
            if (!StringUtils.isNullOrBlank(ptypenames[i]) && ptypenames[i].contains(key)) {
                pagePath = "pages/goods/goods?ptypename=" + key + "&index=" + i;
            }
        }
        if (StringUtils.isNullOrBlank(pagePath)) {
            pagePath = "pages/index/index";
        }
        System.out.println("pagepath===" + pagePath);
        return pagePath;
    }

    public void auto_receive() {
        System.out.println("高级版--自动收货");
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
            if (old_date == null) {
                old_date = order.getCreateDate();
            }
            //判断距离发货日期的时间差 15日
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

    public void getTemplate(Template template, String formId, int sellerId) {
        Seller seller = sellerService.findOne(sellerId);
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=" + seller.getAppId() + "&secret=" + seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);

        //发送模板信息
        template.setForm_id(formId);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + formId;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("高级版访问通知：" + result);
    }

    public void addTemplate(String wxname, String formId, String openId, String message, String pagePath, int sellerId, String templateId) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(wxname);
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(message);
        list.add(templateResponse2);

        Template template = new Template();
        template.setTemplateId(templateId);
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage(pagePath);
        template.setToUser(openId);
        getTemplate(template, formId, sellerId);
    }
}
