package com.haoyue.tuangou.service;

import com.haoyue.tuangou.repo.TPayBackDealRepo;
import com.haoyue.tuangou.wxpay.QTPayBackDeal;
import com.haoyue.tuangou.wxpay.TPayBackDeal;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/17.
 */

@Service
public class TPayBackDealService {

    @Autowired
    private TPayBackDealRepo tPayBackDealRepo;

    public void save(TPayBackDeal payBackDeal) {
        tPayBackDealRepo.save(payBackDeal);
    }

    public Iterable<TPayBackDeal> list(String saleId, int pageNumber) {
        QTPayBackDeal payback=QTPayBackDeal.tPayBackDeal;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(payback.saleId.eq(saleId));
        // pageSize=10 固定
        return tPayBackDealRepo.findAll(bd.getValue(),new PageRequest(pageNumber,10,new Sort(Sort.Direction.DESC,"id")));
    }
}
