package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.repo.ProductsRepo;
import com.haoyue.repo.ProdutsTypeRepo;
import com.haoyue.repo.SellerRepo;
import com.haoyue.untils.*;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    private PtypeNamesService ptypeNamesService;
    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private SellerRepo sellerRepo;

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


    public Iterable<Products> list(Map<String, String> map) {

        QProducts pro = QProducts.products;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(pro.active.eq(true));
        Date date = new Date();
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
                if (name.equals("active")) {
                    bd.and(pro.active.eq(Boolean.valueOf(value)));
                }
                if (name.equals("showdate")) {
                    bd.and(pro.showDate.before(date));
                }
                if (name.equals("showdate_after")) {
                    bd.and(pro.showDate.after(date));
                }
                if (name.equals("secondKillStart")) {
                    bd.and(pro.secondKillStart.before(date));
                }
                if (name.equals("secondKillEnd")) {
                    bd.and(pro.secondKillEnd.after(date));
                }

            }
        }
        return productsRepo.findAll(bd.getValue());
    }

    public Iterable<Products> plist(Map<String, String> map, int pagenumber, int pagesize) {
        QProducts pro = QProducts.products;
        BooleanBuilder bd = new BooleanBuilder();
        Date date = new Date();
        int monthsale_from=0;
        int monthsale_to=0;
        double price_from=0;
        double price_to=0;
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("token")) {
                    bd.and(pro.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("key")) {
                    bd.and(pro.pname.contains(value));
                }
                if (name.equals("pcode")) {
                    bd.and(pro.pcode.contains(value));
                }
                if (name.equals("ptypename")) {
                    bd.and(pro.ptypeName.contains(value));
                }
                // 预售商品放在仓库列表中
                if (name.equals("active")) {
                    bd.and(pro.active.eq(Boolean.valueOf(value)));
                    bd.or(pro.showDate.after(date));
                }
                if (name.equals("mothsale_from")) {
                   monthsale_from=Integer.parseInt(value);
                }
                if (name.equals("mothsale_to")) {
                    monthsale_to=Integer.parseInt(value);
                }
                if (name.equals("price_from")) {
                    price_from=Double.valueOf(value);
                }
                if (name.equals("price_to")) {
                    price_to=Double.valueOf(value);
                }
                if (name.equals("killproduct")) {
                    bd.and(pro.issecondkill.eq(true));
                    bd.and(pro.secondKillStart.before(date));
                    bd.and(pro.secondKillEnd.after(date));
                }
            }
        }
        if(monthsale_from>=0&&monthsale_to!=0){
            bd.and(pro.monthSale.between(monthsale_from,monthsale_to));
        }
        if(price_from>=0&&price_to!=0){
            bd.and(pro.produtsTypes.any().priceNew.between(price_from,price_to));
        }
        return productsRepo.findAll(bd.getValue(), new PageRequest(pagenumber, pagesize, new Sort(Sort.Direction.DESC, new String[]{"monthSale"})));
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
            //商品分类更新
            update_ptype(product);
        }
        //商品上架
        else if (!StringUtils.isNullOrBlank(map.get("active_pro"))) {
            Date date = new Date();
            //如果商品为预售商品，则直接上架后可购买
            if (product.getShowDate().after(date)) {
                product.setShowDate(date);
            }
            product.setActive(true);
            List<ProdutsType> produtsTypes = product.getProdutsTypes();
            for (ProdutsType produtsType : produtsTypes) {
                produtsType.setActive(true);
                produtsTypeRepo.save(produtsType);
            }
            productsRepo.save(product);
            //商品分类更新
            update_ptype(product);
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
            double olddiscount = ptype.getDiscountPrice();
            ptype.setDiscountPrice(Double.valueOf(map.get("discount")));
            //降价
            if (olddiscount != 0 && olddiscount > ptype.getDiscountPrice()) {
                //降价通知
                shopCarService.sendCustomerWxTemplate(ptype.getId(), ptype.getSellerId());
            }
            produtsTypeRepo.save(ptype);
        }

        return new Result(false, Global.do_success, token);
    }

    public void updateList(List<Products> list) {
        productsRepo.save(list);
    }

    public Products findByPcode(String pcode) {
        return productsRepo.findByPcode(pcode);
    }


    public void update_ptype(Products product) {
        //商品分类更新
        List<String> productses = productsRepo.findBySellerIdAndActive(product.getSellerId());
        PtypeNames ptypeNames = ptypeNamesService.findBySellerId(product.getSellerId() + "");
        if (ptypeNames == null) {
            ptypeNames = new PtypeNames();
            ptypeNames.setSellerId(product.getSellerId() + "");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String products : productses) {
            stringBuffer.append(products);
            stringBuffer.append(",");
        }
        ptypeNames.setPtypename(stringBuffer.toString());
        ptypeNamesService.save(ptypeNames);
    }

    public List<Products> findBySellerIdAndCreateDate(String sellerId, Date date) {
        return productsRepo.findBySellerIdAndCreateDate(sellerId, date);
    }

    //生成二维码
    public String qrcode(String sellerId, String pid) throws FileNotFoundException {
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        Seller seller=sellerRepo.findOne(Integer.parseInt(sellerId));
        String param1 = "grant_type=client_credential&appid="+seller.getAppId()+"&secret="+seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        // d:/haoyue/erweima/1.jpg
        String filename = QRcode.getminiqrQr(access_token, pid);
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        OSSClientUtil tossClientUtil = new OSSClientUtil();
        // hymarket/qrcode/xx.jpg
        filename = "qrcodes/" + pid + ".jpg";
        tossClientUtil.uploadFile2OSS(fileInputStream, filename, null);
        //删除已上传文件
        file.delete();
        return filename;
    }

    public void autoFlush() {
        productsRepo.autoFlush(new Date());
    }

    public List<Products> recommend(String sellerId, String pid) {
        List<Products> list = productsRepo.findBySellerIdAndActive(sellerId, true);
        if (!StringUtils.isNullOrBlank(pid)) {
            list.remove(findOne(Integer.parseInt(pid)));
        }
        // < = 四个商品
        if (list.size()<=4){
            return list;
        }
        //  > 四个商品,随机抽取四个商品
        List<Products> newlist = new ArrayList<>();
        int size = list.size();
        int index = 0;
        for (int i = 0; i < 4; i++) {
            index = (int) (Math.random() * size);
            while (newlist.contains(list.get(index))) {
                index = (int) (Math.random() * size);
            }
            newlist.add(list.get(index));
        }
        return newlist;
    }


    public List<Products> findBySellerIdAndPtypeNameAndActive(String sellerId, String ptypename, boolean b) {
        return productsRepo.findBySellerIdAndPtypeNameAndActive(Integer.parseInt(sellerId),ptypename,true);
    }
}
