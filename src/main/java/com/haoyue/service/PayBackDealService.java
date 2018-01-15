package com.haoyue.service;


import com.haoyue.repo.PayBackRepo;
import com.haoyue.wxpay.PayBackDeal;
import com.haoyue.wxpay.QPayBackDeal;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2018/1/4.
 */
@Service
public class PayBackDealService {

    @Autowired
    private PayBackRepo payBackRepo;

    public void save(PayBackDeal payBackDeal){
        payBackRepo.save(payBackDeal);
    }

    public Iterable<PayBackDeal> list(String sellerId, int pageNumber) {
        QPayBackDeal payback=QPayBackDeal.payBackDeal;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(payback.sellerId.eq(sellerId));
        return payBackRepo.findAll(bd.getValue(),new PageRequest(pageNumber,10,new Sort(Sort.Direction.DESC,"id")));
    }
}
