package com.haoyue.web;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.service.CustomProductsTypesService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/12/8.
 */

@RestController
@RequestMapping("/customprotype")
public class CustomProductsTypesController {

    @Autowired
    private CustomProductsTypesService customProductsTypesService;


    // 前台做 分类名 和 商品编号 非空校验
    //  保存 /customprotype/save?name=自定义分类名&sellerId=1221&pcode=商品编号1=商品编号2=商品编号3
    // 编辑  /customprotype/save?name=自定义分类名&sellerId=1221&pcode=商品编号1=商品编号2=商品编号3&id=12
    @RequestMapping("/save")
    public Result save(CustomProductsTypes customProductsTypes) {
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
        return new Result(false, Global.do_success,customProductsTypes,null);
    }

    // 删除  /customprotype/del?saleId=1212&id=121
    @RequestMapping("/del")
    public Result del(String sellerId,String id) {
        CustomProductsTypes customProductsTypes= customProductsTypesService.findOne(Integer.parseInt(id));
        if (!customProductsTypes.getSellerId().equals(sellerId)){
            return new Result(true, Global.have_no_right,null,null);
        }
        customProductsTypesService.del(customProductsTypes);
        return new Result(false, Global.do_success,null,null);
    }

}
