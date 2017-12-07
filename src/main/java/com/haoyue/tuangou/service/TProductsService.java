package com.haoyue.tuangou.service;


import com.haoyue.tuangou.pojo.QTProducts;
import com.haoyue.tuangou.pojo.TProducts;
import com.haoyue.tuangou.repo.TProductsRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/7.
 */
@Service
public class TProductsService {

    @Autowired
    private TProductsRepo tProductsRepo;

    public void save(TProducts tProducts) {
        tProductsRepo.save(tProducts);
    }

    public void update(TProducts tProducts) {
        tProductsRepo.save(tProducts);
    }

    public Iterable<TProducts> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTProducts products=QTProducts.tProducts;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("saleId")){
                    bd.and(products.saleId.eq(value));
                }
                else if (name.equals("active")){
                    bd.and(products.active.eq(Boolean.valueOf(value)));
                }
                else if (name.equals("pname")){
                    value=value.trim();
                    bd.and(products.pname.contains(value));
                }
            }
        }
        return tProductsRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public TProducts findOne(Integer id) {
        return tProductsRepo.findOne(id);
    }

    public List<TProducts> findByTuanProduct(String saleId) {
        return  tProductsRepo.findByTuanProduct(saleId);
    }

    public Iterable<TProducts> findByPname(String pname, String saleId) {
        QTProducts products=QTProducts.tProducts;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(products.saleId.eq(saleId));
        bd.and(products.active.eq(true));
        bd.and(products.pname.contains(pname));
        return tProductsRepo.findAll(bd.getValue());
    }

    public Iterable<TProducts> index(Integer saleId) {
        QTProducts products=QTProducts.tProducts;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(saleId)));
        bd.and(products.active.eq(true));
        return tProductsRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"saleNum"));
    }

    public Iterable<TProducts> news(String saleId) {
        QTProducts products=QTProducts.tProducts;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(saleId)));
        bd.and(products.active.eq(true));
        return tProductsRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public Iterable<TProducts> alls(String saleId, String typeName) {
        QTProducts products=QTProducts.tProducts;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(saleId)));
        bd.and(products.active.eq(true));
        if (!StringUtils.isNullOrBlank(typeName)){
            bd.and(products.types.eq(typeName));
        }
        return tProductsRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"saleNum"));
    }

    public List<TProducts> recommend(String saleId, String pid) {
        List<Integer> ids= findPidsBySaleId(saleId);
        List<TProducts> result=new ArrayList<>();
        List<Integer> newids=new ArrayList<>();
        //如果只有一个商品，则推荐为null
        if (ids.size()==1){
            return null;
        }
        //如果四个商品，推荐剩余的商品
        if (ids.size()<=4){
            for (Integer id:ids){
                if (id==Integer.parseInt(pid)){
                    continue;
                }
                result.add(findOne(ids.get(id)));
            }
            return result;
        }
        //如果多余四个商品，推荐剩余商品中的三个
        for (int i=0;i<3;i++){
            int id=(int)Math.floor(Math.random()*ids.size());
            while (newids.contains(id)||id==Integer.parseInt(pid)){
                id=(int)Math.floor(Math.random()*ids.size());
            }
            newids.add(id);
            result.add(findOne(ids.get(id)));
        }
        return result;
    }

    private List<Integer> findPidsBySaleId(String saleId) {
        return tProductsRepo.findPidsBySaleId(saleId);
    }

    public List<TProducts> recommend2(String saleId) {
        List<Integer> ids= findPidsBySaleId(saleId);
        List<TProducts> result=new ArrayList<>();
        List<Integer> newids=new ArrayList<>();
        if (ids.size()<=3){
            for (Integer id:ids){
                result.add(findOne(ids.get(id)));
            }
            return result;
        }
        for (int i=0;i<3;i++){
            int id=(int)Math.floor(Math.random()*ids.size());
            while (newids.contains(id)){
                id=(int)Math.floor(Math.random()*ids.size());
            }
            newids.add(id);
            result.add(findOne(ids.get(id)));
        }
        return result;
    }

    public void delPtypes(Integer pid) {
        tProductsRepo.delPtypes(pid);
    }

    public List<TProducts> findByTypesAndSaleId(String typename, String saleId) {
        return tProductsRepo.findByTypesAndSaleId(typename,saleId);
    }
}
