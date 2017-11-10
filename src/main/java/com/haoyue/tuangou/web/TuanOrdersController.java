package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.service.TDeliverService;
import com.haoyue.tuangou.service.TProductsService;
import com.haoyue.tuangou.service.TProductsTypesService;
import com.haoyue.tuangou.service.TuanOrdersService;
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



    //   自己创建团购   /tuan/tuanorders/save?pid=商品ID&protypeId=商品分类ID&openId=1&saleId=1
    //   &wxname=微信名称&wxpic=微信头像&productPrice=商品价格&deliverPrice=快递费用
    //   &address=收货地址&receiver=收货人&phone=收货人电话&isowner=true
    //   参团     /tuan/tuanorders/save?pid=商品ID&protypeId=商品分类ID&openId=1&saleId=1
    //   &wxname=微信名称&wxpic=微信头像&productPrice=商品价格&deliverPrice=快递费用
    //   &address=收货地址&receiver=收货人&phone=收货人电话&groupCode=房间号
    @RequestMapping("/save")
    public TResult save(TuanOrders tuanOrders, String pid, String protypeId,TDeliver deliver) {
        TProducts products = tProductsService.findOne(Integer.parseInt(pid));
        TProductsTypes productsTypes = tProductsTypesService.findOne(Integer.parseInt(protypeId));
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
            tuanOrders.setGroupCode(date.getTime() + "");
        } else {
            //参团
            String groupcode=tuanOrders.getGroupCode();
            Map<String,String>  map=new HashMap<>();
            map.put("groupcode",groupcode);
            map.put("isowner","true");
            Iterable<TuanOrders> iterable= tuanOrdersService.filter(map);
            Iterator<TuanOrders> iterator=iterable.iterator();
            TuanOrders orders=iterator.next();
            tuanOrders.setEndDate(orders.getEndDate());
            tuanOrders.setStartDate(orders.getStartDate());
            tuanOrders.setOwner(orders.getOwner());
            tuanOrders.setOwnerpic(orders.getOwnerpic());
        }
        synchronized (TGlobal.object2) {
            tuanOrders.setCode(TGlobal.tuan_ordercode_begin + date.getTime());
            tuanOrdersService.save(tuanOrders);
        }
        return new TResult(false,TGlobal.do_success,null);
    }


    //   /tuan/tuanorders/changestate?oid=团购单ID&state=正在拼团团购订单
    @RequestMapping("/changestate")
    public TResult changeState(String oid,String state){

        TuanOrders orders=tuanOrdersService.findOne(Integer.parseInt(oid));
        //付款
        if (state.equals(TGlobal.tuan_order_tuaning)){
            //判断是否可以开团
            //开团
            if ((orders.getJoinNum()+1)==orders.getStartNum()){
                //更新团购订单
                String groupcode=orders.getGroupCode();
                List<TuanOrders> list=tuanOrdersService.findByGroupCode(groupcode);
                for (TuanOrders tuanOrders:list){
                    tuanOrders.setJoinNum(tuanOrders.getJoinNum()+1);
                    tuanOrders.setIsover(true);
                    if (!tuanOrders.getState().equals(TGlobal.tuan_order_unpay)){
                        tuanOrders.setState(TGlobal.tuan_order_success);
                        //更新商品销量
                        TProducts tProducts=tuanOrders.gettProducts();
                        tProducts.setSaleNum(tProducts.getSaleNum()+1);
                        TProductsTypes tProductsTypes=tuanOrders.gettProductsTypes();
                        tProductsTypes.setSaleNum(tProductsTypes.getSaleNum()+1);
                        tProductsTypesService.update(tProductsTypes);
                        tProductsService.update(tProducts);
                    }
                    tuanOrdersService.update(tuanOrders);
                }
            }else {
                //不开团
                String groupcode=orders.getGroupCode();
                List<TuanOrders> list=tuanOrdersService.findByGroupCode(groupcode);
                for (TuanOrders tuanOrders:list){
                    tuanOrders.setJoinNum(tuanOrders.getJoinNum()+1);
                    tuanOrdersService.update(tuanOrders);
                }
                orders.setState(state);
                tuanOrdersService.update(orders);
            }
        }
        return new TResult(false,TGlobal.do_success,null);
    }


    //   /tuan/tuanorders/del?oid=订单Id&openId=123&saleId=12
    @RequestMapping("/del")
    public TResult  del(String oid,String saleId,String openId){
        TuanOrders tuanOrders=tuanOrdersService.findOne(Integer.parseInt(oid));
        if (!tuanOrders.getOpenId().equals(openId)){
           return new TResult(true,TGlobal.have_no_right,null);
        }
        tuanOrdersService.del(tuanOrders);
        return new TResult(false,TGlobal.do_success,null);
    }

    //   /tuan/tuanorders/list?saleId=12&state=【待发货团购订单，待收货团购订单】
    @RequestMapping("/list")
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable<TuanOrders> iterable=tuanOrdersService.list(map,pageNumber,pageSize);
        return new TResult(false,TGlobal.do_success,iterable);
    }



    //   /tuan/tuanorders/tuaning_list?saleId=12
    @RequestMapping("/tuaning_list")
    public TResult tuaningList(String saleId){
        //所有正在拼团的商品
        List<TProducts> productsList= tProductsService.findByTuanProduct(saleId);
        //遍历商品集合获得正在拼团的房主订单
        List<TuanOrdersResponse> tuanOrdersResponseList=new ArrayList<>();
        List<TuanOrdersResponse> result=new ArrayList<>();
        for (TProducts products:productsList) {
            Iterable<TuanOrders> iterable=tuanOrdersService.findTuanOrdersByTProducts(products.getId(),saleId);
            TuanOrdersResponse tuanOrdersResponse=new TuanOrdersResponse();
            tuanOrdersResponse.setProducts(products);
            tuanOrdersResponse.setIterable(iterable);
            tuanOrdersResponse.setAmount(getIterableLength(iterable));
            tuanOrdersResponseList.add(tuanOrdersResponse);
        }
        //根据拼团房间数排序
       tuanOrdersResponseList.stream()
                .sorted((TuanOrdersResponse p1,TuanOrdersResponse p2)->(p1.getAmount()-p2.getAmount()))
               .forEach((TuanOrdersResponse p)->result.add(p));
        return new TResult(false,TGlobal.do_success,result);
    }

    public int getIterableLength(Iterable iterable){
        int count=0;
        if (iterable==null){
            return count;
        }
        Iterator iterator=iterable.iterator();
        while (iterator.hasNext()){
            count++;
        }
        return count;
    }


}
