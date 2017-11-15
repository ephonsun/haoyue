package com.haoyue.service;

import com.haoyue.pojo.Collections;
import com.haoyue.repo.CollectionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */

@Service
public class CollectionsService {

    @Autowired
    private CollectionsRepo collectionsRepo;

    public void save(Collections collections) {
        collectionsRepo.save(collections);
    }

    public Collections findOne(int id) {
        return collectionsRepo.findOne(id);
    }

    public void del(Collections collections) {
        collectionsRepo.delete(collections);
    }

    public List<Collections> findByOpenIdAndSellerId(String openId, String sellerId) {
        return collectionsRepo.findByOpenIdAndSellerId(openId,sellerId);
    }

    public List<Collections> findBySellerIdAndCreateDate(String sellerId, Date date) {
        return collectionsRepo.findBySellerIdAndCreateDate(sellerId,date);
    }
}
