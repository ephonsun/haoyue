package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.*;
import com.haoyue.repo.ThumbsupRepo;
import com.haoyue.service.*;
import com.haoyue.untils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by LiJia on 2017/8/22.
 */
@RestController
@RequestMapping("/seller/pro")
public class ProductsController {

    @Autowired
    private ProductsService productsService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ThumbsupRepo thumbsupRepo;
    @Autowired
    private PtypeNamesService ptypeNamesService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private LuckDrawService luckDrawService;


    //  /seller/pro/list?pageNumber=页数(后台默认为0)&pageSize=每页显示数(后台默认为10)&token=1&active=true
    //  商品列表-在售  追加参数 showdate=yes
    //   商品列表-预售  追加参数 showdate=no
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, "", productsService.plist(map, pageNumber, pageSize), map.get("token"));
    }

    // http://localhost:8080/seller/pro/uploadPic?token=1&multipartFiles=12221
    @RequestMapping("/uploadPic")
    public Object uploadPic(MultipartFile[] multipartFiles, String token) throws IOException, MyException {
        Seller seller = sellerService.findOne(Integer.parseInt(token));
        if (seller.getUploadFileSize() >= seller.getMaxFileSize()) {
            return new UploadSuccessResult(Global.memoryspace_lack);
        }
        StringBuffer images = new StringBuffer();
        if (multipartFiles.length != 0) {
            for (int i = 0; i < multipartFiles.length; i++) {
                MultipartFile multipartFile = multipartFiles[i];
                Long size = multipartFile.getSize();
                int kb = (int) (size / 1024);
                if (kb + seller.getUploadFileSize() > seller.getMaxFileSize()) {
                    return new UploadFailResult(Global.memoryspace_lack);
                }
                seller.setUploadFileSize(seller.getUploadFileSize() + kb);
                // String type=multipartFile.substring(multipartFile.indexOf("/")+1,multipartFile.indexOf(";"));
                //BASE64Decoder decoder = new BASE64Decoder();
                // byte[] b = decoder.decodeBuffer(multipartFile);
                //返回上传成功的文件名
                //String uploadUrl = new QiNiuUpload().upload(multipartFile.getBytes(),multipartFile.getOriginalFilename());
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                //判断是不是最后一张
                if ((i + 1) == multipartFiles.length) {
                    images.append(Global.aliyun_href + uploadUrl);
                } else {
                    images.append(Global.aliyun_href + uploadUrl);
                    images.append(",");
                }
            }
        }
        System.out.println(images.toString());
        sellerService.update(seller);
        return new UploadSuccessResult(images.toString());
    }

    @RequestMapping("/del")
    public Result del(Integer pid, String token) {
        Products products = productsService.findOne(pid);
        if (products.getSellerId() != Integer.parseInt(token)) {
            return new Result(true, Global.have_no_right, token);
        }
        products.setActive(false);
        productsService.update(products);
        return new Result(true, Global.do_success, token);
    }

    @RequestMapping("/findOne")
    public Result findOne(Integer pid, String token, String pname, String ptype, String pcode) {
        Map<String, String> map = new HashMap<>();
        map.put("pname", pname);
        map.put("ptype", ptype);
        map.put("token", token);
        if (!StringUtils.isNullOrBlank(pcode)) {
            Products products = productsService.findByPcode(pcode);
            if (products.getSellerId() != Integer.parseInt(token)) {
                return new Result(true, Global.have_no_right, token);
            }
            return new Result(false, "", products, token);
        }

        if (!StringUtils.isNullOrBlank(String.valueOf(pid))) {
            Products products = productsService.findOne(pid);
            if (products.getSellerId() != Integer.parseInt(token)) {
                return new Result(true, Global.have_no_right, token);
            }
            return new Result(false, "", products, token);
        } else {
            return new Result(false, Global.do_success, productsService.list(map), null);
        }
    }


    @RequestMapping("/update")
    public Result update(@RequestParam Map<String, String> map) {
        return productsService.updateDesc(map);
    }

    @RequestMapping("/thumbs-up")
    public Result thumbsup(String proId, String openId) {
        Thumbsup thumbsup = thumbsupRepo.findByProIdAndOpenId(proId, openId);
        if (thumbsup == null) {
            //保存新的点赞信息
            thumbsup = new Thumbsup();
            thumbsup.setProId(proId);
            thumbsup.setOpenId(openId);
            thumbsupRepo.save(thumbsup);
            //更改商品点赞量
            Products products = productsService.findOne(Integer.parseInt(proId));
            Integer num = products.getThumbsup();
            num = num == null ? 1 : num++;
            products.setThumbsup(num);
            productsService.update(products);
            return new Result(false, Global.do_success, null, null);
        } else {
            return new Result(false, Global.alery_done, null, null);
        }
    }

    @RequestMapping("/save")
    public Result update_all(Products products, String token, String protypes,Integer showHours) throws FileNotFoundException {
        //上线时间
        if (showHours!=null&&showHours!=0){
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.HOUR,showHours);
            products.setShowDate(calendar.getTime());
        }else {
            products.setShowDate(new Date());
        }

        boolean flag = false;
        if (products.getId() != null) {
            flag = true;
            Products products1 = productsService.findOne(products.getId());
            products.setPcode(products1.getPcode());
        }
        String[] strs = protypes.split("=");
        List<ProdutsType> produtsTypes = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            String[] strings = strs[i].split(",");
            String color = strings[0];//颜色
            String size = strings[1];//尺码
            String discount = strings[2];//折扣价
            String price = strings[3];//原价
            String amount = strings[4];//库存
            if (amount.equals("0")) {
                continue;
            }
            if (!StringUtils.isDiget(discount) || discount.equals("0")) {
                return new Result(true, Global.discount_price_unright, null, null);
            }
            if (!StringUtils.isDiget(price)) {
                return new Result(true, Global.price_is_unright, null, null);
            }
            ProdutsType produtsType = new ProdutsType();
            produtsType.setPriceNew(Double.valueOf(price));
            produtsType.setColor(color);
            //判断是否折扣
            if (!discount.equals("0")) {
                produtsType.setISDiscount(true);
                produtsType.setDiscountPrice(Double.valueOf(discount));
            }
            produtsType.setAmount(Integer.parseInt(amount));
            produtsType.setActive(true);
            produtsType.setPriceOld(0.0);
            produtsType.setSize(size);
            produtsType.setSellerId(Integer.parseInt(token));
            if (produtsType.getDiscountPrice() > produtsType.getPriceNew()) {
                return new Result(true, Global.price_ls_discountprice);
            }
            produtsTypes.add(produtsType);
        }

        products.setProdutsTypes(produtsTypes);
        products.setSellerId(Integer.parseInt(token));
        products.setSellerName(sellerService.findOne(products.getSellerId()).getSellerName());

        try {
            //抽奖是否结束
            if (products.getId()!=null&&products.getIsLuckDraw()==false){
                if (productsService.findOne(products.getId()).getIsLuckDraw()){
                    products.setIsLuckDrawEnd(true);
                    LuckDraw luckDraw=luckDrawService.findBySellerId(products.getSellerId()+"");
                    luckDraw.setJoinNumber(0);
                    luckDrawService.update(luckDraw);
                    orderService.updateIsLuckDrawEnd(products.getId());
                }
            }
            productsService.save(products);
            //商品分类更新
            productsService.update_ptype(products);
            //设置商品号
            if (StringUtils.isNullOrBlank(products.getPcode())) {
                Seller seller = sellerService.findOne(Integer.parseInt(token));
                String str = StringUtils.getPinYinByStr(seller.getSellerName());
                String pcode = str + "-" + (Global.count++) + products.getId();
                boolean f = true;
                //判断新产生的商品号是否存在
                while (f) {
                    Products products1 = productsService.findByPcode(pcode);
                    if (products1 != null && products1.getPcode() != null) {
                        pcode = str + "-" + (Global.count++) + products.getId();
                    } else {
                        f = false;
                    }
                }
                products.setPcode(str + "-" + (Global.count++) + products.getId());
                productsService.update(products);
            }
            //蒋商品信息注入 dictionary
            if (!flag) {
                dictionaryService.addProduct(products);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //更新二维码
        if (StringUtils.isNullOrBlank(products.getQrcode())){
            String url=productsService.qrcode(products.getSellerId()+"",products.getId()+"");
            products.setQrcode(Global.aliyun_href+url);
            productsService.update(products);
        }
        return new Result(false, Global.do_success, products, null);
    }

}
