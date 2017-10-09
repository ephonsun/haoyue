package com.haoyue.service;

import com.haoyue.pojo.Deliver;
import com.haoyue.pojo.Order;
import com.haoyue.repo.DelievrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Deliver save2(Deliver deliver){
        return delievrRepo.save(deliver);
    }


    public void update(Deliver deliver) {
        delievrRepo.save(deliver);
    }
}
