package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TProducts;
import com.haoyue.tuangou.pojo.TProductsTypes;
import com.haoyue.tuangou.repo.TProductsRepo;
import com.haoyue.tuangou.repo.TProductsTypesRepo;
import com.haoyue.tuangou.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/8.
 */
@Service
public class TProductsTypesService {

    @Autowired
    private TProductsTypesRepo tProductsTypesRepo;
    @Autowired
    private TProductsRepo tProductsRepo;

    public void save(TProductsTypes tProductsTypes) {
        tProductsTypesRepo.save(tProductsTypes);
    }

    public void update(TProductsTypes tProductsTypes) {

        TProductsTypes producttype=tProductsTypesRepo.findOne(tProductsTypes.getId());

        if (!StringUtils.isNullOrBlank(tProductsTypes.getNewPrice()+"")){
            producttype.setNewPrice(tProductsTypes.getNewPrice());
        }

        if (!StringUtils.isNullOrBlank(tProductsTypes.getAmount()+"")){
            producttype.setAmount(tProductsTypes.getAmount());
        }

        if (tProductsTypes.getIsActive()==false){
            producttype.setIsActive(false);
        }

        tProductsTypesRepo.save(producttype);
    }

    public TProductsTypes findOne(int id) {
        return tProductsTypesRepo.findOne(id);
    }
}
