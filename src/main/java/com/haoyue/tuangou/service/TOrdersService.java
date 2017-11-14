package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.QTOrders;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.repo.TOrdersRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/11/9.
 */
@Service
public class TOrdersService {

    @Autowired
    private TOrdersRepo  tOrdersRepo;

    public void save(TOrders orders) {
        tOrdersRepo.save(orders);
    }

    public TOrders findOne(int id) {
        return tOrdersRepo.findOne(id);
    }

    public void update(TOrders orders) {
        tOrdersRepo.save(orders);
    }

    public Iterable<TOrders> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTOrders orders=QTOrders.tOrders;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
               if (name.equals("saleId")){
                   bd.and(orders.saleId.eq(value));
                   bd.and(orders.showsale.eq(true));
               }
               else if(name.equals("openId")){
                   bd.and(orders.openId.eq(value));
                   bd.and(orders.showbuy.eq(true));
               }
               else if (name.equals("state")){
                   bd.and(orders.state.eq(value));
               }
               else if (name.equals("code")){
                   bd.and(orders.code.eq(value));
               }

            }
        }

        return tOrdersRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public TOrders findByCode(String code) {
        return tOrdersRepo.findByCode(code);
    }

    public Iterable<TOrders> clist(String saleId, String openId,String unsend) {
        QTOrders order=QTOrders.tOrders;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(order.saleId.eq(saleId));
        bd.and(order.openId.eq(openId));
        bd.and(order.state.eq(TGlobal.order_unpay));
        if (!StringUtils.isNullOrBlank(unsend)){
            bd.and(order.state.eq(TGlobal.order_unsend));
        }
        bd.and(order.showbuy.eq(true));
        return tOrdersRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }
}
