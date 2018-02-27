package com.haoyue.service;

import com.haoyue.pojo.PtypeNames;
import com.haoyue.repo.PtypeNamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiJia on 2017/10/26.
 */
@Service
public class PtypeNamesService {

    @Autowired
    private PtypeNamesRepo ptypeNamesRepo;

    public void save(PtypeNames ptypeNames) {
        ptypeNamesRepo.save(ptypeNames);
    }

    public List<PtypeNames> findBySellerId(String token) {
        return ptypeNamesRepo.findBySellerId(token);
    }

    public void update(PtypeNames p) {
        ptypeNamesRepo.save(p);
    }

    public PtypeNames findBySellerIdAndPtypename(String sellerId, String level2) {
        return ptypeNamesRepo.findBySellerIdAndPtypename(sellerId,level2);
    }

    public PtypeNames findOne(int pid) {
        return ptypeNamesRepo.findOne(pid);
    }

    public PtypeNames findBySellerIdAndPtypenameIsNull(String sellerId) {
        return ptypeNamesRepo.findBySellerIdAndPtypenameIsNull(sellerId);
    }
}
