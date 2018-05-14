package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.ActivityForDiscount;
import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.pojo.Products;
import com.haoyue.service.CustomProductsTypesService;
import com.haoyue.service.ProductsService;
import com.haoyue.untils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by LiJia on 2017/12/8.
 */

@RestController
@RequestMapping("/customprotype")
public class CustomProductsTypesController {

    @Autowired
    private CustomProductsTypesService customProductsTypesService;
    @Autowired
    private ProductsService productsService;


    //  /customprotype/save?name=名称&sellerId=3&(pid=父节点ID)
    @RequestMapping("/save")
    public Result save(CustomProductsTypes customProductsTypes){
        //同步代码块
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean flag = false;
            if (customProductsTypes.getId() == null) {
                customProductsTypes.setCreateDate(new Date());
                flag = true;
            }
            customProductsTypesService.save(customProductsTypes);
            //关联父节点
            if (flag && !StringUtils.isNullOrBlank(customProductsTypes.getPid())) {
                CustomProductsTypes parent = customProductsTypesService.findOne(Integer.parseInt(customProductsTypes.getPid()));
                List<CustomProductsTypes> childs = parent.getChilds();
                List<CustomProductsTypes> news = new ArrayList<>();
                if (childs != null && childs.size() != 0) {
                    news.addAll(childs);
                }
                news.add(customProductsTypes);
                parent.setChilds(news);
                customProductsTypesService.update(parent);
            }
        }
        return new Result(false, null, customProductsTypes, null);
    }


    //  /customprotype/list?sellerId=3(&pageNumber=页数，从0开始)
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<CustomProductsTypes> iterable = customProductsTypesService.list(map, pageNumber, pageSize);
        Iterator<CustomProductsTypes> iterator= iterable.iterator();
        List<CustomProductsTypes> customProductsTypes=new ArrayList<>();
        while (iterator.hasNext()){
            List<CustomProductsTypes> childsnew=new ArrayList<>();
            CustomProductsTypes parent=iterator.next();
            List<CustomProductsTypes> childs= parent.getChilds();
            for (CustomProductsTypes child:childs){
                if (child.getActive()){
                    childsnew.add(child);
                }
            }
            parent.setChilds(childsnew);
            customProductsTypes.add(parent);
        }

        return new Result(false, Global.do_success, customProductsTypes, null);
    }


    //  /customprotype/del?sellerId=3&id=分类ID
    @RequestMapping("/del")
    public Result del(Integer id,String sellerId){
        CustomProductsTypes customProductsTypes=customProductsTypesService.findOne(id);
        customProductsTypes.setActive(false);
        customProductsTypesService.update(customProductsTypes);

        //如果删除二级分类，取消关联商品和二级分类
        if(!StringUtils.isNullOrBlank(customProductsTypes.getPid())){
            productsService.del_childtypes_middle(customProductsTypes.getId());
        }else {
            //如果删除一级分类，首先获取二级分类
            List<CustomProductsTypes> childs=customProductsTypes.getChilds();
            //取消关联二级分类
            if(childs!=null&&childs.size()!=0){
                for (CustomProductsTypes child:childs){
                    productsService.del_childtypes_middle(child.getId());
                }
            }
            //取消关联一级分类
            productsService.del_parenttypes_middle(customProductsTypes.getId());

        }

        return new Result(false, Global.do_success, null, null);
    }

    //  /customprotype/bind?sellerId=3&id=二级分类ID&pid=一级分类ID&productid=商品Id
    @RequestMapping("bind")
    public Result bind(Integer pid,Integer id,Integer productid,String sellerId){

        Products products=productsService.findOne(productid);
        CustomProductsTypes parent=customProductsTypesService.findOne(pid);
        CustomProductsTypes child=customProductsTypesService.findOne(id);
        //商品关联一级分类
        List<CustomProductsTypes> parenttypes =products.getParenttypes();
        List<CustomProductsTypes> parenttypes1=new ArrayList<>();
        if(parenttypes!=null&&parenttypes.size()!=0){
            parenttypes1.addAll(parenttypes);
        }
        if(parenttypes==null||!parenttypes.contains(parent)) {
            parenttypes1.add(parent);
        }
        //商品关联二级分类
        List<CustomProductsTypes> childtypes =products.getChildtypes();
        List<CustomProductsTypes> childtypes1=new ArrayList<>();
        if(childtypes!=null&&childtypes.size()!=0){
            childtypes1.addAll(childtypes);
        }
        if(childtypes==null||!childtypes.contains(child)) {
            childtypes1.add(child);
        }
        products.setParenttypes(parenttypes1);
        products.setChildtypes(childtypes1);
        //提交更新
        productsService.update(products);

        return new Result(false, Global.do_success, null, null);
    }


    //  /customprotype/unbind?pid=商品ID&(parentid=一级分类/childid=二级分类ID）&sellerId=3
    @RequestMapping("unbind")
    public Result unbind(Integer pid,Integer parentid,Integer childid,String sellerId){
        if(parentid!=0) {
            productsService.unbind_parenttypes_middle(parentid,pid);
        }
        if(childid!=0){
            productsService.unbind_childtypes_middle(childid,pid);
        }
        return new Result(false, Global.do_success, null, null);
    }


    //上传图片  /customprotype/uploadPics?multipartFiles=图片
    @RequestMapping("/uploadPics")
    public UploadSuccessResult uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                stringBuffer.append(",");
            }
        }
        return new UploadSuccessResult(stringBuffer.toString());
    }

    //一级分类关联海报和关键字
    // /customprotype/setinfos?id=一级分类ID&pics=图片地址&sellerId=3&keys=关键字1,关键字2,关键字3
    @RequestMapping("/setinfos")
    public Result setPics(Integer id,String pics,String sellerId,String keys){
        CustomProductsTypes customProductsTypes=customProductsTypesService.findOne(id);
        customProductsTypes.setPics(pics);
        customProductsTypes.setKeys(keys);
        customProductsTypesService.update(customProductsTypes);
        return new Result(false, Global.do_success, customProductsTypes, null);
    }


}
