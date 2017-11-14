package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.service.*;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    //   /tuan/torders/save?pid=商品ID&ptypeId=商品分类ID&amount=购买数量&productPrice=下单的商品价格
    //    &deliverPrice=快递费用,免邮则0&openId=12&saleId=12&wxname=微信名&wxpic=微信头像
    //    &address=收货地址&receiver=收货人&phone=收货人电话
    //   前台做下单量和库存量对比
    @RequestMapping("/save")
    public TResult save(TOrders tOrders, String pid, String ptypeId, TDeliver tdeliver) {
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
            orders.setProductPrice(tOrders.getProductPrice());
            TProducts tProducts = tProductsService.findOne(Integer.parseInt(pid));
            TProductsTypes tProductsTypes = tProductsTypesService.findOne(Integer.parseInt(ptypeId));
            orders.settProducts(tProducts);
            orders.settProductsTypes(tProductsTypes);
            orders.setTotalPrice(orders.getDeliverPrice() + orders.getProductPrice() * orders.getAmount());
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
        }
        tOrdersService.update(orders);
        return new TResult(false, TGlobal.do_success, orders);
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
        String unsend = "yes";
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
        while (iterator2.hasNext()){
            TMixOrders tMixOrders = new TMixOrders();
            TuanOrders tuanOrders=iterator2.next();
            tMixOrders.setOid(tuanOrders.getId());
            tMixOrders.setDate(tuanOrders.getStartDate());
            tMixOrders.setIstuan(true);
            list.add(tMixOrders);
        }
        if (list.size()==0){
            return new TResult(false, TGlobal.do_success, null);
        }
        //对TMixOrders中的数据按照时间排序
        List<TMixOrders> list2=new ArrayList<>();
        list.stream()
                .sorted((p1,p2)->(p1.getDate().compareTo(p2.getDate())))
                .forEach(p->list2.add(p));
        //遍历list2，获取订单，封装结果集objects
        List<Object> objects=new ArrayList<>();
        for (int i=0;i<list2.size();i++){
            TMixOrders tMixOrders=list2.get(i);
            if (tMixOrders.istuan()){
                //团购订单
                objects.add(tuanOrdersService.findOne(tMixOrders.getOid()));
            }else {
                //普通订单
                objects.add(tOrdersService.findOne(tMixOrders.getOid()));
            }
        }
        return new TResult(false, TGlobal.do_success, objects);
    }
    
    // // TODO: 2017/11/13 待收货 
}

