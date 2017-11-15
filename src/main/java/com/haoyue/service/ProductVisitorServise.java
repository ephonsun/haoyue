package com.haoyue.service;

import com.haoyue.pojo.ProductVisitor;
import com.haoyue.repo.ProductVisitorRepo;
import com.haoyue.tuangou.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */

@Service
public class ProductVisitorServise {

    @Autowired
    private ProductVisitorRepo productVisitorRepo;

    public ProductVisitor findByOpenIdAndPidAndCreateDate(String openId, String pid) {
        Date now = new Date();
        Date date = StringUtils.getYMD(now);
        return productVisitorRepo.findByOpenIdAndPidAndCreateDate(openId, pid, date);
    }


    public void save(ProductVisitor productVisitor) {
        productVisitorRepo.save(productVisitor);
    }

    public List<ProductVisitor> findBySellerIdAndCreateDate(String sellerId, Date date) {
        return productVisitorRepo.findBySellerIdAndCreateDate(sellerId,date);
    }
}
