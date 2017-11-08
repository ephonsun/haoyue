package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TProductsTypesName;
import com.haoyue.tuangou.repo.TProductsTypesNameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/8.
 */
@Service
public class TProductsTypesNameService {

    @Autowired
    private TProductsTypesNameRepo tProductsTypesNameRepo;

    public TProductsTypesName findBySaleId(String saleId) {
        return tProductsTypesNameRepo.findBySaleId(saleId);
    }

    public void update(String saleId,String typename){
        TProductsTypesName tProductsTypesName=tProductsTypesNameRepo.findBySaleId(saleId);
        if (tProductsTypesName==null){
            tProductsTypesName=new TProductsTypesName();
            tProductsTypesName.setCreateDate(new Date());
            tProductsTypesName.setSaleId(saleId);
            tProductsTypesName.setTypes(typename);
        }else {
            String typenames=tProductsTypesName.getTypes();
            if (!typenames.contains(typename)){
                typenames=typenames+","+typename;
                tProductsTypesName.setTypes(typenames);
            }
        }
        tProductsTypesNameRepo.save(tProductsTypesName);
    }

    public void update2(TProductsTypesName tProductsTypesName) {
        tProductsTypesNameRepo.save(tProductsTypesName);
    }
}
