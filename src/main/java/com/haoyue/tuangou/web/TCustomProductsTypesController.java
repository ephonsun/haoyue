package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TCustomProductsTypes;
import com.haoyue.tuangou.service.TCustomProductsTypesService;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/12/8.
 */

@RestController
@RequestMapping("/tuan/customprotype")
public class TCustomProductsTypesController {

    @Autowired
    private TCustomProductsTypesService customProductsTypesService;


    // 前台做 分类名 和 商品编号 非空校验
    //  保存  /tuan/customprotype/save?name=自定义分类名&saleId=1221&pcode=商品编号1=商品编号2=商品编号3
    // 编辑  /tuan/customprotype/save?name=自定义分类名&saleId=1221&pcode=商品编号1=商品编号2=商品编号3&id=12
    @RequestMapping("/save")
    public TResult save(TCustomProductsTypes customProductsTypes) {
        customProductsTypes.setCreateDate(new Date());
        String pcode = customProductsTypes.getPcode();
        String pcodes[] = pcode.split("=");
        for (String str : pcodes) {
            if (!StringUtils.isNullOrBlank(str)) {
                pcode += "=" + str;
            }
        }
        customProductsTypes.setPcode(pcode.substring(1));
        customProductsTypesService.save(customProductsTypes);
        return new TResult(false, TGlobal.do_success,null);
    }

    // 删除  /tuan/customprotype/del?saleId=1212&id=121
    @RequestMapping("/del")
    public TResult del(String saleId,String id) {
        TCustomProductsTypes customProductsTypes= customProductsTypesService.findOne(Integer.parseInt(id));
        if (!customProductsTypes.getSaleId().equals(saleId)){
            return new TResult(true, TGlobal.have_no_right,null);
        }
        customProductsTypesService.del(customProductsTypes);
        return new TResult(false, TGlobal.do_success,null);
    }

}
