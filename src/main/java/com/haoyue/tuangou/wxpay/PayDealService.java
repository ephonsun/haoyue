package com.haoyue.tuangou.wxpay;

import com.haoyue.untils.StringUtils;
import com.haoyue.wxpay.QPayDeal;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/9/19.
 */
@Service
public class PayDealService {

    @Autowired
    private PayDealRepo payDealRepo;

    public void save(PayDeal payDeal) {
        payDealRepo.save(payDeal);
    }

    public Iterable<PayDeal> list(Map<String, String> map , int pageNumber, int pageSize) {

        QPayDeal payDeal=QPayDeal.payDeal;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("appId")){
                    bd.and(payDeal.appId.eq(value));
                }
                if (name.equals("transaction_id")){
                    bd.and(payDeal.transaction_id.eq(value));
                }
            }
        }

        return payDealRepo.findAll(bd.getValue(),new PageRequest(pageNumber, pageSize));

    }

}
