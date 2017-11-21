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

import java.util.*;

/**
 * Created by LiJia on 2017/11/9.
 */
@RestController
@RequestMapping("/tuan/tuanorders")
public class TuanOrdersController {

    @Autowired
    private TuanOrdersService tuanOrdersService;
    @Autowired
    private TProductsService tProductsService;
    @Autowired
    private TProductsTypesService tProductsTypesService;
    @Autowired
    private TDeliverService tDeliverService;
    @Autowired
    private TDictionarysService tDictionarysService;


    //   自己创建团购   /tuan/tuanorders/save?pid=商品ID&protypeId=商品分类ID&openId=1&saleId=1
    //   &wxname=微信名称&wxpic=微信头像&productPrice=商品价格&deliverPrice=快递费用
    //   &address=收货地址&receiver=收货人&phone=收货人电话&isowner=true
    //   参团     /tuan/tuanorders/save?pid=商品ID&protypeId=商品分类ID&openId=1&saleId=1
    //   &wxname=微信名称&wxpic=微信头像&productPrice=商品价格&deliverPrice=快递费用
    //   &address=收货地址&receiver=收货人&phone=收货人电话&groupCode=房间号
    @RequestMapping("/save")
    public TResult save(TuanOrders tuanOrders, String pid, String protypeId, TDeliver deliver) {

        //判断用户openId是否为空
        if (StringUtils.isNullOrBlank(tuanOrders.getOpenId())||tuanOrders.getOpenId().equals("undefined")){
            return new TResult(true,TGlobal.openid_isnull,null);
        }

        TProducts products = tProductsService.findOne(Integer.parseInt(pid));
        TProductsTypes productsTypes = tProductsTypesService.findOne(Integer.parseInt(protypeId));
        synchronized (TGlobal.object2) {
            Date date = new Date();
            //物流
            deliver.setCreateDate(date);
            deliver.setSaleId1(tuanOrders.getSaleId());
            deliver.setOpenId1(tuanOrders.getOpenId());
            tDeliverService.save(deliver);

            tuanOrders.settDeliver(deliver);
            tuanOrders.setState(TGlobal.tuan_order_unpay);
            tuanOrders.setStartNum(products.getTuanNumbers());
            tuanOrders.setTotalPrice(tuanOrders.getProductPrice() + tuanOrders.getDeliverPrice());
            tuanOrders.settProducts(products);
            tuanOrders.settProductsTypes(productsTypes);
            tuanOrders.setHours(products.getTuanTimes());

            //房主
            if (tuanOrders.getIsowner()) {
                tuanOrders.setOwner(tuanOrders.getWxname());
                tuanOrders.setOwnerpic(tuanOrders.getWxpic());
                tuanOrders.setStartDate(date);
                int hour = products.getTuanTimes();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, hour);
                tuanOrders.setEndDate(calendar.getTime());
                tuanOrders.setGroupCode(new Date().getTime() + "");
            } else {
                //参团
                String groupcode = tuanOrders.getGroupCode();
                Map<String, String> map = new HashMap<>();
                map.put("groupcode", groupcode);
                map.put("isowner", "true");
                Iterable<TuanOrders> iterable = tuanOrdersService.filter(map);
                Iterator<TuanOrders> iterator = iterable.iterator();
                TuanOrders orders = iterator.next();
                //判断时间
                if (orders.getStartDate().before(date)) {
                    return new TResult(true, TGlobal.tuan_time_too_late, null);
                }
                //判断人数
                if (orders.getJoinNum() + 1 > orders.getStartNum()) {
                    return new TResult(true, TGlobal.tuan_num_too_late, null);
                }
                tuanOrders.setEndDate(orders.getEndDate());
                tuanOrders.setStartDate(orders.getStartDate());
                tuanOrders.setOwner(orders.getOwner());
                tuanOrders.setOwnerpic(orders.getOwnerpic());
            }

            tuanOrders.setCode(TGlobal.tuan_ordercode_begin + date.getTime());
            tuanOrdersService.save(tuanOrders);
        }
        return new TResult(false, TGlobal.do_success, tuanOrders);
    }


    //   /tuan/tuanorders/changestate?oid=团购单ID&state=正在拼团团购订单
    @RequestMapping("/changestate")
    public TResult changeState(String oid, String state) {
        TuanOrders orders = null;
        synchronized (TGlobal.object2) {
            orders = tuanOrdersService.findOne(Integer.parseInt(oid));
            //付款
            if (state.equals(TGlobal.tuan_order_tuaning)) {
                //如果人数超过开团人数
                if ((orders.getJoinNum() + 1) > orders.getStartNum()) {
                    return new TResult(true, TGlobal.tuan_num_too_late, null);
                }
                //判断是否可以开团
                //开团
                if ((orders.getJoinNum() + 1) == orders.getStartNum()) {
                    //更新团购订单
                    String groupcode = orders.getGroupCode();
                    List<TuanOrders> list = tuanOrdersService.findByGroupCode(groupcode);
                    for (TuanOrders tuanOrders : list) {
                        tuanOrders.setJoinNum(tuanOrders.getJoinNum() + 1);
                        tuanOrders.setIsover(true);
                        if (!tuanOrders.getState().equals(TGlobal.tuan_order_unpay)) {
                            tuanOrders.setState(TGlobal.tuan_order_success);
                            //更新商品销量
                            TProducts tProducts = tuanOrders.gettProducts();
                            tProducts.setSaleNum(tProducts.getSaleNum() + 1);
                            TProductsTypes tProductsTypes = tuanOrders.gettProductsTypes();
                            tProductsTypes.setSaleNum(tProductsTypes.getSaleNum() + 1);
                            tProductsTypes.setAmount(tProductsTypes.getAmount() - 1);
                            tProductsTypesService.update(tProductsTypes);
                            tProductsService.update(tProducts);
                        }
                        tuanOrdersService.update(tuanOrders);
                    }
                } else {
                    //不开团
                    String groupcode = orders.getGroupCode();
                    List<TuanOrders> list = tuanOrdersService.findByGroupCode(groupcode);
                    for (TuanOrders tuanOrders : list) {
                        tuanOrders.setJoinNum(tuanOrders.getJoinNum() + 1);
                        tuanOrdersService.update(tuanOrders);
                    }
                    orders.setState(state);
                    tuanOrdersService.update(orders);
                }
                //微信付款通知
                addTemplate(orders);
                //更新tdictionary表
                TDictionarys tDictionarys = tDictionarysService.findByTodaySaleId(orders.getSaleId());
                tDictionarys.setTurnover(tDictionarys.getTurnover() + orders.getTotalPrice());
                tDictionarysService.update(tDictionarys);
            }
        }
        return new TResult(false, TGlobal.do_success, null);
    }

    //   /tuan/tuanorders/del?oid=订单Id&openId=123&saleId=12
    @RequestMapping("/del")
    public TResult del(String oid, String saleId, String openId) {
        TuanOrders tuanOrders = tuanOrdersService.findOne(Integer.parseInt(oid));
        if (!tuanOrders.getOpenId().equals(openId)) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        tuanOrdersService.del(tuanOrders);
        return new TResult(false, TGlobal.do_success, null);
    }

    //   /tuan/tuanorders/list?saleId=12&state=【待发货团购订单，待收货团购订单】
    @RequestMapping("/list")
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<TuanOrders> iterable = tuanOrdersService.list(map, pageNumber, pageSize);
        Iterator<TuanOrders> iterator = iterable.iterator();
        Map<String, List<TuanOrders>> map1 = new HashMap<>();
        Map<String, List<TuanOrders>> map2 = new HashMap<>();
        Map<TProducts, List<TuanOrders>> map3 = new HashMap<>();
        String code = "";
        // 首先结果集封装成 key(code)-value(List<TuanOrders>)形式
        while (iterator.hasNext()) {
            TuanOrders tuanOrders = iterator.next();
            code = tuanOrders.getCode();
            List<TuanOrders> values = map1.get(code);
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(tuanOrders);
            map1.put(code, values);
        }
        // 再次封装结果 key(pid)-value(List<TuanOrders>)形式
        for (String key : map1.keySet()) {
            List<TuanOrders> list = map1.get(key);
            String pid = list.get(0).gettProducts().getId() + "";
            if (map2.get(pid) == null) {
                map2.put(pid, list);
            } else {
                List<TuanOrders> newlist = new ArrayList<>();
                List<TuanOrders> oldlist = map2.get(code);
                newlist.addAll(oldlist);
                newlist.addAll(list);
                map2.put(pid, newlist);
            }
        }
        // 最后封装结果 key(TProducts)-value(List<TuanOrders>)形式
        for (String key : map2.keySet()) {
            TProducts tProducts = tProductsService.findOne(Integer.parseInt(key));
            map3.put(tProducts, map2.get(key));
        }

        return new TResult(false, TGlobal.do_success, map3);
    }


    //   /tuan/tuanorders/tuaning_list?saleId=12
    @RequestMapping("/tuaning_list")
    public TResult tuaningList(String saleId) {
        //所有正在拼团的商品
        List<TProducts> productsList = tProductsService.findByTuanProduct(saleId);
        //遍历商品集合获得正在拼团的房主订单
        List<TuanOrdersResponse> tuanOrdersResponseList = new ArrayList<>();
        List<TuanOrdersResponse> result = new ArrayList<>();
        for (TProducts products : productsList) {
            Iterable<TuanOrders> iterable = tuanOrdersService.findTuanOrdersByTProducts(products.getId(), saleId, "yes");
            TuanOrdersResponse tuanOrdersResponse = new TuanOrdersResponse();
            tuanOrdersResponse.setProducts(products);
            tuanOrdersResponse.setIterable(iterable);
            tuanOrdersResponse.setAmount(getIterableLength(iterable));
            tuanOrdersResponseList.add(tuanOrdersResponse);
        }
        //根据拼团房间数排序
        tuanOrdersResponseList.stream()
                .sorted((TuanOrdersResponse p1, TuanOrdersResponse p2) -> (p1.getAmount() - p2.getAmount()))
                .forEach((TuanOrdersResponse p) -> result.add(p));
        return new TResult(false, TGlobal.do_success, result);
    }

    public int getIterableLength(Iterable iterable) {
        int count = 0;
        if (iterable == null) {
            return count;
        }
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            count++;
        }
        return count;
    }

    //   /tuan/tuanorders/tuaning_clist?saleId=12&openId=12
    @RequestMapping("/tuaning_clist")
    public TResult clist(String saleId, String openId) {
        Iterable<TuanOrders> iterable = tuanOrdersService.tuaning_clist(saleId, openId);
        return new TResult(false, TGlobal.do_success, iterable);
    }


    //   /tuan/tuanorders/finsh?oid=订单ID&openId=12
    @RequestMapping("/finsh")
    public TResult finsh(String oid, String openId) {
        TuanOrders tuanOrders = tuanOrdersService.findOne(Integer.parseInt(oid));
        if (!tuanOrders.getOpenId().equals(openId)) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        tuanOrders.setState(TGlobal.tuan_order_finsh);
        tuanOrdersService.update(tuanOrders);
        return new TResult(true, TGlobal.do_success, null);
    }

    //   /tuan/tuanorders/buydel?oid=订单ID&openId=12
    @RequestMapping("/buydel")
    public TResult delTuanorder(String oid, String openId) {
        TuanOrders tuanOrders = tuanOrdersService.findOne(Integer.parseInt(oid));
        if (!tuanOrders.getOpenId().equals(openId)) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        tuanOrders.setShowbuy(false);
        tuanOrdersService.update(tuanOrders);
        return new TResult(false, TGlobal.do_success, null);
    }


    //人为设置拼团成功，根据房间号
    //   /tuan/tuanorders/setsuccess?groupCode=团购号&saleId=123
    @RequestMapping("/setsuccess")
    public TResult setSuccessByGroupCode(String groupCode, String saleId) {
        List<TuanOrders> list = tuanOrdersService.findByGroupCode(groupCode);
        if (list != null && list.size() != 0) {
            if (!list.get(0).getSaleId().equals(saleId)) {
                return new TResult(true, TGlobal.have_no_right, null);
            }
            for (TuanOrders orders : list) {
                orders.setIsover(true);
                orders.setState(TGlobal.tuan_order_success);
                tuanOrdersService.update(orders);
            }
        }
        return new TResult(false, TGlobal.do_success, null);
    }


    public void addTemplate(TuanOrders order) {
        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(order.gettProducts().getPname());
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(order.getWxname());
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(order.getTotalPrice() + "");
        list.add(templateResponse3);

        TemplateResponse templateResponse4 = new TemplateResponse();
        templateResponse4.setColor("#000000");
        templateResponse4.setName("keyword4");
        templateResponse4.setValue("微信支付");
        list.add(templateResponse4);

        TemplateResponse templateResponse5 = new TemplateResponse();
        templateResponse5.setColor("#000000");
        templateResponse5.setName("keyword5");
        String date = com.haoyue.untils.StringUtils.formDateToStr(new Date());
        templateResponse5.setValue(date);
        list.add(templateResponse5);

        Template template = new Template();
        template.setTemplateId("Z_Xg6rYdQgci4FP_aOjTvZHXeC5BSs99EwARD6NJXWk");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(order.getOpenId());
        getTemplate(template);
    }

    public void getTemplate(Template template) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxe46b9aa1b768e5fe&secret=8bcdb74a9915b5685fa0ec37f6f25b24";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id = TGlobal.tuan_package_map.get(template.getToUser());
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        //删除该key-value
        TGlobal.tuan_package_map.remove(template.getToUser());
    }

}
