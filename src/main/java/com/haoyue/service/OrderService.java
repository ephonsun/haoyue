package com.haoyue.service;

import com.haoyue.pojo.Order;
import com.haoyue.pojo.QOrder;
import com.haoyue.repo.OrderRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public Iterable<Order> list(Map<String, String> map , int pageNumber, int pageSize) {

        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("id")){
                    bd.and(order.id.eq(Integer.parseInt(value)));
                }
                if (name.equals("token")){
                    Date now=new Date();
                    Date befor=new Date();
                    befor.setDate(1);
                    befor.setHours(0);
                    befor.setMinutes(0);
                    befor.setSeconds(0);
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                    bd.and(order.createDate.between(befor,now));
                }
                if (name.equals("state")){
                    bd.and(order.state.eq(value));
                }
                if (name.equals("sellerId")){
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
            }
        }

        return orderRepo.findAll(bd.getValue(),new PageRequest(pageNumber, pageSize));
    }

    public Iterable<Order> clist(Map<String, String> map ) {

        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("cid")){
                    bd.and(order.customerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("state")){
                    if (!value.equals("全部订单")) {
                        bd.and(order.state.contains(value));

                    }
                }
                if (name.equals("sellerId")){
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("active")){
                    bd.and(order.active.eq(true));
                }
            }
        }

        return orderRepo.findAll(bd.getValue());
    }

    public Order findOne(Integer id) {
        return orderRepo.findOne(id);
    }

    public void del(Integer id) {
        orderRepo.delete(id);
    }

    public void update(Order order) {
        orderRepo.save(order);
    }

    public Order save(Order order) {
        return orderRepo.save(order);
    }
}
