package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TDeliver;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.pojo.TProducts;
import com.haoyue.tuangou.pojo.TProductsTypes;
import com.haoyue.tuangou.service.TDeliverService;
import com.haoyue.tuangou.service.TOrdersService;
import com.haoyue.tuangou.service.TProductsService;
import com.haoyue.tuangou.service.TProductsTypesService;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            orders.setTotalPrice(orders.getDeliverPrice() + orders.getProductPrice()*orders.getAmount());
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


}
