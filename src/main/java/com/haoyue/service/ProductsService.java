package com.haoyue.service;

import com.haoyue.pojo.Products;
import com.haoyue.pojo.ProdutsType;
import com.haoyue.pojo.QProducts;
import com.haoyue.pojo.Seller;
import com.haoyue.repo.ProductsRepo;
import com.haoyue.repo.ProdutsTypeRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/22.
 */
@Service
public class ProductsService {

    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private ProdutsTypeRepo produtsTypeRepo;

    public Products save(Products products) throws IOException {

        if (products.getId() == null) {
            Date date = new Date();
            products.setCreateDate(date);
        }
        List<ProdutsType> produtsTypeList = products.getProdutsTypes();
        if (products.getId() != null) {

            produtsTypeRepo.deleteByProId(products.getId());
        }
        produtsTypeRepo.save(produtsTypeList);
        productsRepo.save(products);
        for (ProdutsType produtsType : produtsTypeList) {
            produtsType.setProductId(products.getId());
        }
         produtsTypeRepo.save(produtsTypeList);
         return products;
    }

    public Products findOne(Integer id) {
        return productsRepo.findOne(id);
    }

    public void update(Products products) {
        productsRepo.save(products);
    }


//   后期可能会用到
//    public String desc(String productionDesc) throws IOException {
//
//        String[] splits = productionDesc.split("&");
//        productionDesc = "";
//        //判断是图片还是文字
//        for (int i = 0; i < splits.length; i++) {
//            String str = splits[i];
//            if (str.contains("base64") && str.contains("data:image")) {
//                String filename = str.substring(str.indexOf("/") + 1, str.lastIndexOf(";"));
//                String base64 = str.split(",")[1];
//                BASE64Decoder decode = new BASE64Decoder();
//                byte[] b = null;
//                try {
//                    b = decode.decodeBuffer(base64);
//                    String uploadUrl = new QiNiuUpload().upload(b, "." + filename);
//                    splits[i] = Global.aliyun_href + uploadUrl;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if ((i + 1) == splits.length) {
//                productionDesc += splits[i];
//            } else {
//                productionDesc += splits[i] + "&";
//            }
//        }
//        return productionDesc;
//    }


    public Iterable<Products> list(Map<String, String> map) {

        QProducts pro = QProducts.products;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(pro.active.eq(true));
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("token")) {
                    bd.and(pro.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("key")) {
                    bd.and(pro.pname.contains(value));
                }
                if (name.equals("ptypename")) {
                    bd.and(pro.ptypeName.contains(value));
                }
                if (name.equals("pname")) {
                    bd.and(pro.pname.contains(value));
                }
                if (name.equals("ptype")) {
                    bd.and(pro.ptypeName.contains(value));
                }

            }
        }
        return productsRepo.findAll(bd.getValue());
    }

    public Iterable<Products> plist(Map<String, String> map, int pagenumber, int pagesize) {
        QProducts pro = QProducts.products;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("token")) {
                    bd.and(pro.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("key")) {
                    bd.and(pro.pname.contains(value));
                }
                if (name.equals("ptypename")) {
                    bd.and(pro.ptypeName.contains(value));
                }
                if (name.equals("active")) {
                    bd.and(pro.active.eq(Boolean.valueOf(value)));
                }
            }
        }
        return productsRepo.findAll(bd.getValue(), new PageRequest(pagenumber, pagesize, new Sort(Sort.Direction.DESC, new String[]{"monthSale"})));
    }

    public Result downProduct(String id, String token) {
        Products product = productsRepo.findOne(Integer.parseInt(id));
        if (product.getSellerId() != Integer.parseInt(token)) {
            return new Result(true, Global.have_no_right, token);
        }
        product.setActive(false);
        productsRepo.save(product);
        return new Result(false, Global.do_success, token);
    }

    public Result updateDesc(Map<String, String> map) {
        Products product = null;
        product = productsRepo.findOne(Integer.parseInt(map.get("pid")));
        String token = map.get("token");
        if (product.getSellerId() != Integer.parseInt(token)) {
            return new Result(true, Global.have_no_right, token);
        }
        //下架 商品分类 下架
        if (!StringUtils.isNullOrBlank(map.get("downptype"))) {
            Integer proTypeId = Integer.parseInt(map.get("protypeId"));
            ProdutsType produtsType = produtsTypeRepo.findOne(proTypeId);
            produtsType.setActive(false);
            //product.setActive(false);
            produtsTypeRepo.save(produtsType);
        }
        //商品下架
        else if (!StringUtils.isNullOrBlank(map.get("downpro"))) {
            product.setActive(false);
            productsRepo.save(product);
        }
        //商品上架
        else if (!StringUtils.isNullOrBlank(map.get("active_pro"))){
            product.setActive(true);
            productsRepo.save(product);
        }
        //单价
        else if (!StringUtils.isNullOrBlank(map.get("price"))) {
            ProdutsType ptype = produtsTypeRepo.findOne(Integer.parseInt(map.get("ptype")));
            ptype.setPriceOld(ptype.getPriceNew());
            ptype.setPriceNew(Double.valueOf(map.get("price")));
            productsRepo.save(product);
        }
        //库存
        else if (!StringUtils.isNullOrBlank(map.get("amount"))) {
            ProdutsType ptype = produtsTypeRepo.findOne(Integer.parseInt(map.get("ptype")));
            ptype.setAmount(Integer.parseInt(map.get("amount")));
            produtsTypeRepo.save(ptype);
        }
        //打折
        else if (!StringUtils.isNullOrBlank(map.get("discount"))) {
            ProdutsType ptype = produtsTypeRepo.findOne(Integer.parseInt(map.get("ptypeId")));
            ptype.setISDiscount(true);
            ptype.setDiscountPrice(Double.valueOf(map.get("discount")));
            produtsTypeRepo.save(ptype);
        }
        return new Result(false, Global.do_success, token);
    }

    public void updateList(List<Products> list) {
        productsRepo.save(list);
    }
}
