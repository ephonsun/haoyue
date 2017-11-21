package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TProductsTypes;
import com.haoyue.tuangou.service.TProductsTypesService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/11/8.
 */
@RestController
@RequestMapping("/tuan/produttypes")
public class TProductsTypesController {
    @Autowired
    private TProductsTypesService tProductsTypesService;


    @RequestMapping("/update")
    public TResult update(TProductsTypes tProductsTypes, String saleId) {
        tProductsTypesService.update(tProductsTypes);
        return new TResult(false, TGlobal.do_success,null);
    }
}
