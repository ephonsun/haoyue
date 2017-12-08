package com.haoyue.service;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.repo.CustomProductsTypesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiJia on 2017/12/8.
 */
@Service
public class CustomProductsTypesService {

    @Autowired
    private CustomProductsTypesRepo customProductsTypesRepo;

    public void save(CustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.save(customProductsTypes);
    }

    public CustomProductsTypes findOne(int id) {
        return customProductsTypesRepo.findOne(id);
    }

    public void del(CustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.delete(customProductsTypes);
    }

    public List<CustomProductsTypes> findBySellerId(String sellerId) {
        return customProductsTypesRepo.findBySellerId(sellerId);
    }
}
