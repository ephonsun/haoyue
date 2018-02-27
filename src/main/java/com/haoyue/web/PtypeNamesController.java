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

import java.util.ArrayList;
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


    // http://localhost:8080/ptypename/save?sellerId=1&ptypename=男士服装&ptypenames=下衣
    @RequestMapping("/save")
    public Result save(PtypeNames ptypeNames) {

        //判断一级分类是否存在
       PtypeNames parent= ptypeNamesService.findBySellerIdAndPtypename(ptypeNames.getSellerId(),ptypeNames.getPtypename());
       //存在更新二级分类
       if (parent!=null){
           //获取二级分类
           String pnames=parent.getPtypenames();
           if (StringUtils.isNullOrBlank(pnames)){
               pnames=ptypeNames.getPtypenames();
           }else {
               if (!pnames.contains(parent.getPtypenames())){
                   pnames=pnames+","+ptypeNames.getPtypenames();
               }
           }
           parent.setPtypenames(pnames);
       }
       else {
           parent=ptypeNames;
       }
       ptypeNamesService.save(parent);
       return new Result(false, Global.do_success, null, null);
    }

    // http://localhost:8080/ptypename/list?sellerId=1
    @RequestMapping("/list")
    public Result list(String sellerId, String openId) {
        String token = sellerId;
        //结果集
        List<PtypeNames> ptypeNames=new ArrayList<>();
        //所有一级分类拼接成的字符串记录
        PtypeNames parents=ptypeNamesService.findBySellerIdAndPtypenameIsNull(sellerId);
        if (parents!=null){
            List<PtypeNames> ptypeNamesList=new ArrayList<>();
            for (String str:parents.getPtypenames().split(",")){
                ptypeNamesList.add(ptypeNamesService.findBySellerIdAndPtypename(sellerId,str));
            }
            ptypeNames=ptypeNamesList;
        }else {
              ptypeNames = ptypeNamesService.findBySellerId(token);
        }
        //自定义分类
//        List<CustomProductsTypes> customProductsTypes = customProductsTypesService.findBySellerId(sellerId);
//        if (customProductsTypes != null && customProductsTypes.size() != 0 && !StringUtils.isNullOrBlank(openId)) {
//            for (CustomProductsTypes ptypenames : customProductsTypes) {
//                ptypeNames.setPtypename(ptypenames.getName()+","+ptypeNames.getPtypename());
//            }
//        }
        return new Result(false, Global.do_success, ptypeNames, token);
    }

    // http://localhost:8080/ptypename/changeSort_2?pid=6&sellerId=1&pnames=上衣,西装,下衣
    //改变二级分类顺序
    @RequestMapping("/changeSort_2")
    public Result changeSort2(int pid,String pnames,String sellerId){
       PtypeNames ptypeNames= ptypeNamesService.findOne(pid);
       ptypeNames.setPtypenames(pnames);
       ptypeNamesService.save(ptypeNames);
       return new Result(false, Global.do_success, null, null);
    }

    // http://localhost:8080/ptypename/changeSort?sellerId=1&pnames=女士服装,男士服装
    //改变一级分类顺序
    @RequestMapping("/changeSort")
    public Result changeSort(String pnames,String sellerId){
        PtypeNames ptypeNames= ptypeNamesService.findBySellerIdAndPtypenameIsNull(sellerId);
        if (ptypeNames==null){
            ptypeNames=new PtypeNames();
            ptypeNames.setSellerId(sellerId);
        }
        ptypeNames.setPtypenames(pnames);
        ptypeNamesService.save(ptypeNames);
        return new Result(false, Global.do_success, null, null);
    }

}
