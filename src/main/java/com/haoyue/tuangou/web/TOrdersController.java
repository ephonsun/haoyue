package com.haoyue.tuangou.web;


import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.service.*;
import com.haoyue.tuangou.utils.CommonUtil;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import com.haoyue.tuangou.wxpay.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by LiJia on 2017/11/9.
 */
@RestController
@RequestMapping("/tuan/torders")
public class TOrdersController {

    @Autowired
    private TOrdersService tOrdersService;
    @Autowired
    private TProductsService tProductsService;
    @Autowired
    private TProductsTypesService tProductsTypesService;
    @Autowired
    private TDeliverService tDeliverService;
    @Autowired
    private TuanOrdersService tuanOrdersService;
    @Autowired
    private TDictionarysService tDictionarysService;
    @Autowired
    private TFreeShoppingService tFreeShoppingService;
    @Autowired
    private TCouponService tcouponService;

    //   /tuan/torders/save?pid=商品ID&ptypeId=商品分类ID&amount=购买数量&productPrice=下单的商品价格
    //    &deliverPrice=快递费用,免邮则0&openId=12&saleId=12&wxname=微信名&wxpic=微信头像
    //    &address=收货地址&receiver=收货人&phone=收货人电话&couponId=优惠券ID&leavemsg=买家留言&formId=121212
    //   前台做下单量和库存量对比

    @RequestMapping("/save")
    @Transactional
    public TResult save(TOrders tOrders, String pid, String ptypeId, TDeliver tdeliver, String couponId) {
        //判断用户openId是否为空
        if (StringUtils.isNullOrBlank(tOrders.getOpenId()) || tOrders.getOpenId().equals("undefined")) {
            return new TResult(true, TGlobal.openid_isnull, null);
        }
        TOrders orders = new TOrders();
        // 加入线程锁，避免code重复
        synchronized (TGlobal.object) {
            orders.setCreateDate(new Date());
            orders.setWxpic(tOrders.getWxpic());
            orders.setWxname(tOrders.getWxname());
            orders.setAmount(tOrders.getAmount());
            orders.setDeliverPrice(tOrders.getDeliverPrice());
            orders.setOpenId(tOrders.getOpenId());
            orders.setSaleId(tOrders.getSaleId());
            orders.setFormId(tOrders.getFormId());
            orders.setProductPrice(tOrders.getProductPrice());
            TProducts tProducts = tProductsService.findOne(Integer.parseInt(pid));
            TProductsTypes tProductsTypes = tProductsTypesService.findOne(Integer.parseInt(ptypeId));
            orders.settProducts(tProducts);
            orders.settProductsTypes(tProductsTypes);
            orders.setTotalPrice(orders.getDeliverPrice() + orders.getProductPrice() * orders.getAmount());
            //是否使用优惠券
            if (!StringUtils.isNullOrBlank(couponId)) {
                TCoupon coupon = tcouponService.findOne(Integer.parseInt(couponId));
                if (coupon.getEndDate().before(new Date())) {
                    return new TResult(true, TGlobal.coupon_expire, null);
                }
                orders.setTotalPrice(orders.getTotalPrice() - coupon.getMoney());
                //更新优惠券信息
                coupon.setIsuse(true);
                tcouponService.save(coupon);
            }
            orders.setState(TGlobal.order_unpay);
            orders.setCode(TGlobal.ordercode_begin + new Date().getTime());
        }


        //物流
        tdeliver.setCreateDate(new Date());
        tdeliver.setOpenId1(orders.getOpenId());
        tdeliver.setSaleId1(orders.getSaleId());
        tDeliverService.save(tdeliver);
        //订单保存
        orders.settDeliver(tdeliver);
        tOrdersService.save(orders);
        return new TResult(false, TGlobal.do_success, orders);
    }

    //   /tuan/torders/changestate?oid=订单ID&state=【待发货订单 已完成订单】
    @RequestMapping("/changestate")
    public TResult changeState(String oid, String state) {
        TOrders orders = tOrdersService.findOne(Integer.parseInt(oid));
        orders.setState(state);
        // 付款成功转待发货订单
        if (state.equals(TGlobal.order_unsend)) {
            orders.setPayDate(new Date());
            //更新商品卖出量,库存量
            TProducts products = orders.gettProducts();
            TProductsTypes productsTypes = orders.gettProductsTypes();
            products.setSaleNum(products.getSaleNum() + orders.getAmount());
            productsTypes.setSaleNum(productsTypes.getSaleNum() + orders.getAmount());
            productsTypes.setAmount(productsTypes.getAmount() - orders.getAmount());
            tProductsService.update(products);
            tProductsTypesService.save(productsTypes);
            //微信通知
            addTemplate(orders);
            //// TODO: 2017/11/28 存储零元购信息  失效日期
            freeorder(orders);
            //更新tdictionary表
            TDictionarys tDictionarys = tDictionarysService.findByTodaySaleId(orders.getSaleId());
            tDictionarys.setTurnover(tDictionarys.getTurnover() + orders.getTotalPrice());
            tDictionarys.setBuyers(tDictionarys.getBuyers() + 1);
            tDictionarysService.update(tDictionarys);
        }
        tOrdersService.update(orders);
        return new TResult(false, TGlobal.do_success, orders);
    }

    public void freeorder(TOrders orders) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 8);
        TFreeShopping freeShopping = new TFreeShopping();
        freeShopping.setCreateDate(date);
        freeShopping.setEndDate(calendar.getTime());
        freeShopping.setOpenId(orders.getOpenId());
        freeShopping.setOrderCode1(orders.getCode());
        freeShopping.setSaleId(orders.getSaleId());
        tFreeShoppingService.save(freeShopping);
    }

    //   /tuan/torders/del?oid=订单ID&【openId 买家删除订单   saleId 卖家删除订单】
    @RequestMapping("/del")
    public TResult del(String oid, String openId, String saleId) {
        TOrders orders = tOrdersService.findOne(Integer.parseInt(oid));
        if (!StringUtils.isNullOrBlank(openId)) {
            orders.setShowbuy(false);
        }
        if (!StringUtils.isNullOrBlank(saleId)) {
            orders.setShowsale(false);
        }
        tOrdersService.update(orders);
        return new TResult(false, TGlobal.do_success, null);
    }


    //   卖家-普通订单-未发货 /tuan/torders/list?saleId=12&state=待发货订单
    //   卖家-普通订单-已发货 /tuan/torders/list?saleId=12&state=待收货订单
    //   卖家-普通订单-已完成 /tuan/torders/list?saleId=12&state=已完成订单&showsale=true
    @RequestMapping("/list")
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<TOrders> iterable = tOrdersService.list(map, pageNumber, pageSize);
        return new TResult(false, TGlobal.do_success, iterable);
    }

    //  普通订单-查看详情 /tuan/torders/findone?oid=12
    @RequestMapping("/findone")
    public TResult findOne(String oid) {
        TOrders orders = tOrdersService.findOne(Integer.parseInt(oid));
        return new TResult(false, TGlobal.do_success, orders);
    }


    //普通订单-待付款  /tuan/torders/unpay?saleId=12&openId=21
    @RequestMapping("/unpay")
    public TResult unpay(String saleId, String openId) {
        Iterable<TOrders> iterable1 = tOrdersService.clist(saleId, openId, null);
        return new TResult(false, TGlobal.do_success, iterable1);
    }


    //   /tuan/torders/unsend?saleId=12&openId=21
    @RequestMapping("/unsend")
    public TResult unsend(String saleId, String openId) {
        //普通订单未发货
        String unsend = "unsend";
        Iterable<TOrders> iterable1 = tOrdersService.clist(saleId, openId, unsend);
        //团购订单待发货
        Iterable<TuanOrders> iterable2 = tuanOrdersService.unsend(saleId, openId);
        //抽出两个结果集的ID和创建时间，放进TMixOrders
        List<TMixOrders> list = new ArrayList<>();
        Iterator<TOrders> iterator = iterable1.iterator();
        Iterator<TuanOrders> iterator2 = iterable2.iterator();
        while (iterator.hasNext()) {
            TMixOrders tMixOrders = new TMixOrders();
            TOrders tOrders = iterator.next();
            tMixOrders.setDate(tOrders.getCreateDate());
            tMixOrders.setOid(tOrders.getId());
            list.add(tMixOrders);
        }
        while (iterator2.hasNext()) {
            TMixOrders tMixOrders = new TMixOrders();
            TuanOrders tuanOrders = iterator2.next();
            tMixOrders.setOid(tuanOrders.getId());
            tMixOrders.setDate(tuanOrders.getStartDate());
            tMixOrders.setIstuan(true);
            list.add(tMixOrders);
        }
        if (list.size() == 0) {
            return new TResult(false, TGlobal.do_success, null);
        }
        //对TMixOrders中的数据按照时间排序
        List<TMixOrders> list2 = new ArrayList<>();
        list.stream()
                .sorted((p1, p2) -> (p1.getDate().compareTo(p2.getDate())))
                .forEach(p -> list2.add(p));
        //遍历list2，获取订单，封装结果集objects
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            TMixOrders tMixOrders = list2.get(i);
            if (tMixOrders.istuan()) {
                //团购订单
                objects.add(tuanOrdersService.findOne(tMixOrders.getOid()));
            } else {
                //普通订单
                objects.add(tOrdersService.findOne(tMixOrders.getOid()));
            }
        }
        return new TResult(false, TGlobal.do_success, objects);
    }

    //   /tuan/torders/unreceive?saleId=12&openId=21
    @RequestMapping("/unreceive")
    public TResult unreceive(String saleId, String openId) {
        //普通订单待收货
        String unreceive = "unreceive";
        Iterable<TOrders> iterable1 = tOrdersService.clist(saleId, openId, unreceive);
        //团购订单待收货
        Iterable<TuanOrders> iterable2 = tuanOrdersService.unreceive(saleId, openId);
        //抽出两个结果集的ID和创建时间，放进TMixOrders
        List<TMixOrders> list = new ArrayList<>();
        Iterator<TOrders> iterator = iterable1.iterator();
        Iterator<TuanOrders> iterator2 = iterable2.iterator();
        while (iterator.hasNext()) {
            TMixOrders tMixOrders = new TMixOrders();
            TOrders tOrders = iterator.next();
            tMixOrders.setDate(tOrders.getCreateDate());
            tMixOrders.setOid(tOrders.getId());
            list.add(tMixOrders);
        }
        while (iterator2.hasNext()) {
            TMixOrders tMixOrders = new TMixOrders();
            TuanOrders tuanOrders = iterator2.next();
            tMixOrders.setOid(tuanOrders.getId());
            tMixOrders.setDate(tuanOrders.getStartDate());
            tMixOrders.setIstuan(true);
            list.add(tMixOrders);
        }
        if (list.size() == 0) {
            return new TResult(false, TGlobal.do_success, null);
        }
        //对TMixOrders中的数据按照时间排序
        List<TMixOrders> list2 = new ArrayList<>();
        list.stream()
                .sorted((p1, p2) -> (p1.getDate().compareTo(p2.getDate())))
                .forEach(p -> list2.add(p));
        //遍历list2，获取订单，封装结果集objects
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            TMixOrders tMixOrders = list2.get(i);
            if (tMixOrders.istuan()) {
                //团购订单
                objects.add(tuanOrdersService.findOne(tMixOrders.getOid()));
            } else {
                //普通订单
                objects.add(tOrdersService.findOne(tMixOrders.getOid()));
            }
        }
        return new TResult(false, TGlobal.do_success, objects);
    }


    //  已完成订单 /tuan/torders/finsh?saleId=12&openId=21
    @RequestMapping("/finsh")
    public TResult finsh(String saleId, String openId) {
        //普通订单已完成
        String finsh = "finsh";
        Iterable<TOrders> iterable1 = tOrdersService.clist(saleId, openId, finsh);
        //团购订单已完成
        Iterable<TuanOrders> iterable2 = tuanOrdersService.finsh(saleId, openId);
        //抽出两个结果集的ID和创建时间，放进TMixOrders
        List<TMixOrders> list = new ArrayList<>();
        Iterator<TOrders> iterator = iterable1.iterator();
        Iterator<TuanOrders> iterator2 = iterable2.iterator();
        while (iterator.hasNext()) {
            TMixOrders tMixOrders = new TMixOrders();
            TOrders tOrders = iterator.next();
            tMixOrders.setDate(tOrders.getCreateDate());
            tMixOrders.setOid(tOrders.getId());
            list.add(tMixOrders);
        }
        while (iterator2.hasNext()) {
            TMixOrders tMixOrders = new TMixOrders();
            TuanOrders tuanOrders = iterator2.next();
            tMixOrders.setOid(tuanOrders.getId());
            tMixOrders.setDate(tuanOrders.getStartDate());
            tMixOrders.setIstuan(true);
            list.add(tMixOrders);
        }
        if (list.size() == 0) {
            return new TResult(false, TGlobal.do_success, null);
        }
        //对TMixOrders中的数据按照时间排序
        List<TMixOrders> list2 = new ArrayList<>();
        list.stream()
                .sorted((p1, p2) -> (p1.getDate().compareTo(p2.getDate())))
                .forEach(p -> list2.add(p));
        //遍历list2，获取订单，封装结果集objects
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            TMixOrders tMixOrders = list2.get(i);
            if (tMixOrders.istuan()) {
                //团购订单
                objects.add(tuanOrdersService.findOne(tMixOrders.getOid()));
            } else {
                //普通订单
                objects.add(tOrdersService.findOne(tMixOrders.getOid()));
            }
        }
        return new TResult(false, TGlobal.do_success, objects);
    }


    //付款通知
    public void addTemplate(TOrders order) {
        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(order.getCode());
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(order.gettProducts().getPname());
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(order.getTotalPrice() + "");
        list.add(templateResponse3);

        TemplateResponse templateResponse4 = new TemplateResponse();
        templateResponse4.setColor("#000000");
        templateResponse4.setName("keyword4");
        templateResponse4.setValue(StringUtils.formDateToStr(order.getCreateDate()));
        list.add(templateResponse4);

        String message = "您的商品很快就飞奔到您手上咯";
        String page = "pages/index/index";
//        if (order.getTotalPrice()>0){
//            message="恭喜你获得一次0元购的机会，有效期8小时，点击免费挑选...暂未开启";
//            page="pages/index/index";
//        }
        TemplateResponse templateResponse5 = new TemplateResponse();
        templateResponse5.setColor("#000000");
        templateResponse5.setName("keyword5");
        templateResponse5.setValue(message);
        list.add(templateResponse5);

        Template template = new Template();
        template.setTemplateId("e4-w5VmQdVU8PABNhB0SBYF4D-0kvFC-bRBuD5nWpUE");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage(page);
        template.setToUser(order.getOpenId());
        getTemplate(template, null, order.getSaleId());
    }


    public void getTemplate(Template template, String formId, String saleId) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id = "";
        if (StringUtils.isNullOrBlank(formId)) {
            form_id = TGlobal.tuan_package_map.get(template.getToUser());
        } else {
            form_id = formId;
        }
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("团实惠-普通订单微信模板result:" + result);
        //删除该key-value
        TGlobal.tuan_package_map.remove(template.getToUser());
    }


    //卖家后台查询
    //  /tuan/torders/query?order_type=【1 普通订单 2 团购订单】&【code 单号 / startDate,endDate / wxname 买家昵称】
    @RequestMapping("/query")
    public TResult query(@RequestParam Map<String, String> map) {

        if (map.get("order_type").equals("1")) {
            Iterable<TOrders> iterable = tOrdersService.query(map);
            return new TResult(false, TGlobal.do_success, iterable);
        }
        if (map.get("order_type").equals("2")) {
            Iterable<TuanOrders> iterable = tuanOrdersService.query(map);
            return new TResult(false, TGlobal.do_success, iterable);
        }
        return new TResult(false, TGlobal.do_success, null);
    }


    //   /tuan/torders/delay?openId=122&oid=订单ID
    @RequestMapping("/delay")
    public TResult delay(String openId, String oid) {
        TOrders orders = tOrdersService.findOne(Integer.parseInt(oid));
        if (!orders.getOpenId().equals(openId)) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        if (orders.getIsdelay()) {
            return new TResult(true, TGlobal.already_delay, null);
        }
        orders.setIsdelay(true);
        tOrdersService.save(orders);
        return new TResult(false, TGlobal.do_success, null);
    }

    //   /tuan/torders/leavemsg?oid=订单ID&saleId=122&leavemsg=卖家留言
    @RequestMapping("/leavemsg")
    public TResult leavemsg(String saleId, String oid, String leavemsg) {
        TOrders orders = tOrdersService.findOne(Integer.parseInt(oid));
        if (!orders.getSaleId().equals(saleId)) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        orders.setLeavemsg2(leavemsg);
        tOrdersService.update(orders);
        return new TResult(false, TGlobal.do_success, null);
    }

    //   http://localhost:8080/tuan/torders/excel?saleId=1&oids=订单ID1=订单ID2=订单ID3
    @RequestMapping("/excel")
    public TResult excel(String saleId, String oids) throws IOException {
        return tOrdersService.excel(saleId, oids);
    }


}

