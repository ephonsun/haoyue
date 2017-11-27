package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TProductsTypesName;
import com.haoyue.tuangou.service.TProductsTypesNameService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/11/8.
 */
@RestController
@RequestMapping("/tuan/types")
public class TProductsTypesNameController {

    @Autowired
    private TProductsTypesNameService tProductsTypesNameService;

    //   /tuan/types/list?saleId=12
    @RequestMapping("/list")
    public TResult list(String saleId){
        TProductsTypesName tProductsTypesName=tProductsTypesNameService.findBySaleId(saleId);
        return new TResult(false, TGlobal.do_success,tProductsTypesName);
    }

    //  /tuan/types/update?saleId=1&typenames=裤子,皮鞋,帽子
    @RequestMapping("/update")
    public void update(String saleId,String typenames){
        TProductsTypesName tProductsTypesName=tProductsTypesNameService.findBySaleId(saleId);
        tProductsTypesName.setTypes(typenames);
        tProductsTypesNameService.update2(tProductsTypesName);
    }

}
