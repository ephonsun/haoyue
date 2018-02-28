package com.haoyue.service;

import com.haoyue.pojo.PtypeNames;
import com.haoyue.repo.PtypeNamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PtypeNames findBySellerId(String token) {
        return ptypeNamesRepo.findBySellerId(token);
    }
}
