package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TCollections;
import com.haoyue.tuangou.repo.TCollectionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/8.
 */
@Service
public class TCollectionsService {

    @Autowired
    private TCollectionsRepo tCollectionsRepo;

    public TCollections findByCidAndSaleId(Integer cid, String saleId) {
        return tCollectionsRepo.findByCidAndSaleId(String.valueOf(cid),saleId);
    }

    public void save(TCollections collections) {
        tCollectionsRepo.save(collections);
    }

    public void update(TCollections tCollections) {
        tCollectionsRepo.save(tCollections);
    }

    public void delByCollectionIdAndPid(Integer cid, Integer pid) {
        tCollectionsRepo.delByCollectionIdAndPid(cid,pid);
    }
}
