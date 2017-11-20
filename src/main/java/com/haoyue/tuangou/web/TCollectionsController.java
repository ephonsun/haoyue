package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TCollections;
import com.haoyue.tuangou.pojo.TProducts;
import com.haoyue.tuangou.pojo.TUserBuy;
import com.haoyue.tuangou.service.TCollectionsService;
import com.haoyue.tuangou.service.TProductsService;
import com.haoyue.tuangou.service.TUserBuyService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/8.
 */
@RestController
@RequestMapping("/tuan/collection")
public class TCollectionsController {

    @Autowired
    private TCollectionsService tCollectionsService;
    @Autowired
    private TUserBuyService tUserBuyService;
    @Autowired
    private TProductsService tProductsService;

    // /tuan/collection/save?saleId=1&pid=商品id&openId=12
    @RequestMapping("/save")
    public TResult save(TCollections tCollections,String pid,String openId){
        TUserBuy  tUserBuy=tUserBuyService.findByOpenIdAndSaleId(openId,tCollections.getSaleId());
        TCollections collections=tCollectionsService.findByCidAndSaleId(tUserBuy.getId(),tCollections.getSaleId());
        Date date=new Date();
        //第一次收藏
        if (collections==null){
            collections=new TCollections();
            collections.setCreateDate(date);
            collections.setSaleId(tCollections.getSaleId());
            collections.setCid(tUserBuy.getId()+"");
            List<TProducts>  list=new ArrayList<>();
            TProducts tProducts=tProductsService.findOne(Integer.parseInt(pid));
            list.add(tProducts);
            collections.setProductses(list);
        }else {
            List<TProducts>  oldlist=collections.getProductses();
            List<TProducts>  newlist=new ArrayList<>();
            newlist.addAll(oldlist);
            newlist.add(tProductsService.findOne(Integer.parseInt(pid)));
            collections.setProductses(newlist);
        }
        collections.setUpdateDate(date);
        tCollectionsService.save(collections);
        return new TResult(false, TGlobal.do_success,null);
    }



    // /tuan/collection/findone?openId=ook0P0YZbNNbUzPMNjNDIr0auSLo&saleId=2
    @RequestMapping("/findone")
    public TResult findOne(String saleId,String openId){
        TUserBuy  tUserBuy=tUserBuyService.findByOpenIdAndSaleId(openId,saleId);
        TCollections tCollections=tCollectionsService.findByCidAndSaleId(tUserBuy.getId(),saleId);
        return new TResult(false, TGlobal.do_success,tCollections);
    }



    // /tuan/collection/cancel?openId=1&saleId=1&pid=商品Id
    @RequestMapping("/cancel")
    public TResult cancel(String pid,String openId,String saleId){
        TUserBuy  tUserBuy=tUserBuyService.findByOpenIdAndSaleId(openId,saleId);
        TCollections tCollections=tCollectionsService.findByCidAndSaleId(tUserBuy.getId(),saleId);
        List<TProducts>  oldlist=tCollections.getProductses();
        List<TProducts>  newlist=new ArrayList<>();
        for (TProducts products:oldlist){
            if (products.getId()==Integer.parseInt(pid)){
                continue;
            }else {
                newlist.add(products);
            }
        }
        tCollections.setProductses(newlist);
        tCollectionsService.update(tCollections);
        return new TResult(false, TGlobal.do_success,tCollections);
    }

}
