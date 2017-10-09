package com.haoyue.service;

import com.haoyue.pojo.ShopCarDetail;
import com.haoyue.repo.ShopCarDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/9/4.
 */
@Service
public class ShopCarDetailService {

    @Autowired
    private ShopCarDetailRepo shopCarDetailRepo;

    public void update(ShopCarDetail shopCarDetail) {
        shopCarDetailRepo.save(shopCarDetail);
    }

    public void save(ShopCarDetail shopcardetail) {
        shopCarDetailRepo.save(shopcardetail);
    }
}
