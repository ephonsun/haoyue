package com.haoyue.service;

import com.haoyue.pojo.ProductDailyRecord;
import com.haoyue.repo.ProductDailyRecordRepo;
import com.haoyue.tuangou.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
@Service
public class ProductDailyRecordService {

    @Autowired
    private ProductDailyRecordRepo productDailyRecordRepo;

    public ProductDailyRecord findByPidAndSellerIdAndDate(String pid, String sellerId) {
        Date now=new Date();
        Date date=StringUtils.getYMD(now);
        return productDailyRecordRepo.findByPidAndSellerIdAndDate(Integer.parseInt(pid),sellerId,date);
    }

    public void save(ProductDailyRecord productDailyRecord) {
        productDailyRecordRepo.save(productDailyRecord);
    }

    public List<ProductDailyRecord> findBySellerIdAndCreateDate(String sellerId,Date now) {
        Date date=StringUtils.getYMD(now);
        return productDailyRecordRepo.findBySellerIdAndCreateDate(sellerId,date);
    }
}
