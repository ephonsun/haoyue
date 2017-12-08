package com.haoyue.tuangou.web;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.tuangou.pojo.TCustomProductsTypes;
import com.haoyue.tuangou.pojo.TProductsTypesName;
import com.haoyue.tuangou.service.TCustomProductsTypesService;
import com.haoyue.tuangou.service.TProductsTypesNameService;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.haoyue.pojo.QPtypeNames.ptypeNames;

/**
 * Created by LiJia on 2017/11/8.
 */
@RestController
@RequestMapping("/tuan/types")
public class TProductsTypesNameController {

    @Autowired
    private TProductsTypesNameService tProductsTypesNameService;
    @Autowired
    private TCustomProductsTypesService customProductsTypesService;

    //   /tuan/types/list?saleId=12
    @RequestMapping("/list")
    public TResult list(String saleId,String openId){
        TProductsTypesName tProductsTypesName=tProductsTypesNameService.findBySaleId(saleId);
        //自定义分类
        List<TCustomProductsTypes> customProductsTypes = customProductsTypesService.findBySaleId(saleId);
        if (customProductsTypes != null && customProductsTypes.size() != 0 && !StringUtils.isNullOrBlank(openId)) {
            for (TCustomProductsTypes ptypenames : customProductsTypes) {
                tProductsTypesName.setTypes(ptypenames.getName()+","+tProductsTypesName.getTypes());
            }
        }
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
