package com.haoyue.service;

import com.haoyue.pojo.Deliver;
import com.haoyue.pojo.Order;
import com.haoyue.repo.DelievrRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class DelievrService {
    @Autowired
    private DelievrRepo delievrRepo;
    @Autowired
    private OrderService orderService;

    public Deliver save(Deliver deliver, Order order) {
        delievrRepo.save(deliver);
        order.setDeliver(deliver);
        orderService.update(order);
        return deliver;
    }

    public Deliver save2(Deliver deliver) {
        return delievrRepo.save(deliver);
    }

    public void update(Deliver deliver) {
        delievrRepo.save(deliver);
    }

    public Deliver findByDcodeAndDename(String dcode, String dename) {
        return delievrRepo.findByDcodeAndDename(dcode, dename);
    }

    public List findBySellerId(String sellerId) {
        List<String> dnames = delievrRepo.findDnamesBySellerId(sellerId);
        List<Object> list = new ArrayList<>();
        for (String dname : dnames) {
            list.add(delievrRepo.findBySellerIdAndDname(sellerId, dname));
        }
        return list;
    }

    public List<String> findDnamesBySellerId(String sellerId) {
        return delievrRepo.findDnamesBySellerId(sellerId);
    }

    public void deleteByDnameAndSellerId(String dname, String sellerId) {
        delievrRepo.deleteByDnameAndSellerId(dname, sellerId);
    }

    public List<Deliver> findBySellerIdAndDname(String token, String deliver_name) {
        return delievrRepo.findBySellerIdAndDname(token, deliver_name);
    }

    public Result delLine(String id, String seller_id) {
        Deliver deliver = delievrRepo.findOne(Integer.parseInt(id));
        if (!deliver.getSellerId().equals(seller_id)) {
            return new Result(true, Global.have_no_right, null, null);
        }
        delievrRepo.delete(deliver);
        return new Result(false, Global.do_success, null, null);
    }

    public Deliver findOne(int id) {
        return delievrRepo.findOne(id);
    }
}
