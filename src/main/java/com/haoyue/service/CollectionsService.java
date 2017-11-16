package com.haoyue.service;

import com.haoyue.pojo.Collection;
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

    public void save(Collection collection) {
        collectionsRepo.save(collection);
    }

    public Collection findOne(int id) {
        return collectionsRepo.findOne(id);
    }

    public void del(Collection collection) {
        collectionsRepo.delete(collection);
    }

    public List<Collection> findByOpenIdAndSellerId(String openId, String sellerId) {
        return collectionsRepo.findByOpenIdAndSellerId(openId,sellerId);
    }

    public List<Collection> findBySellerIdAndCreateDate(String sellerId, Date date) {
        return collectionsRepo.findBySellerIdAndCreateDate(sellerId,date);
    }
}
