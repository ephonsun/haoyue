package com.haoyue.web;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.pojo.PtypeNames;
import com.haoyue.service.CustomProductsTypesService;
import com.haoyue.service.PtypeNamesService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by LiJia on 2017/10/26.
 */
@RestController
@RequestMapping("/ptypename")
public class PtypeNamesController {

    @Autowired
    private PtypeNamesService ptypeNamesService;
    @Autowired
    private CustomProductsTypesService customProductsTypesService;

    @RequestMapping("/save")
    public Result save(PtypeNames ptypeNames) {
        ptypeNamesService.save(ptypeNames);
        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/list")
    public Result list(String sellerId, String openId) {
        String token = sellerId;
        PtypeNames ptypeNames = ptypeNamesService.findBySellerId(token);
        //自定义分类
        List<CustomProductsTypes> customProductsTypes = customProductsTypesService.findBySellerId(sellerId);
        if (customProductsTypes != null && customProductsTypes.size() != 0 && !StringUtils.isNullOrBlank(openId)) {
            for (CustomProductsTypes ptypenames : customProductsTypes) {
                ptypeNames.setPtypename(ptypenames.getName()+","+ptypeNames.getPtypename());
            }
        }
        return new Result(false, Global.do_success, ptypeNames, token);
    }

}
