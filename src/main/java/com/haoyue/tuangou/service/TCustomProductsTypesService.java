package com.haoyue.tuangou.service;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.tuangou.pojo.TCustomProductsTypes;
import com.haoyue.tuangou.repo.TCustomProductsTypesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiJia on 2017/12/8.
 */
@Service
public class TCustomProductsTypesService {

    @Autowired
    private TCustomProductsTypesRepo customProductsTypesRepo;

    public void save(TCustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.save(customProductsTypes);
    }

    public TCustomProductsTypes findOne(int id) {
        return customProductsTypesRepo.findOne(id);
    }

    public void del(TCustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.delete(customProductsTypes);
    }



    public List<TCustomProductsTypes> findBySaleId(String saleId) {
        return customProductsTypesRepo.findBySaleId(saleId);
    }
}
