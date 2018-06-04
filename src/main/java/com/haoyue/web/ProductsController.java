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

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
    private OrderService orderService;
    @Autowired
    private LuckDrawService luckDrawService;
    @Autowired
    private ShopCarService shopCarService;


    //  https://www.cslapp.com/seller/pro/list?token=3&key=商品名称&pcode=商品编号&
    //  mothsale_from=销量开始&mothsale_to=销量结束&price_from=价格开始&price_to=价格结束
    //  &ptypename=宝贝分类
    //  商品列表-在售  追加参数 showdate=yes
    //  商品列表-预售  追加参数 showdate=no
    // 秒杀商品列表  追加参数 killproduct=true
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Products> iterable=productsService.plist(map, pageNumber, pageSize);
        Iterator<Products> iterator= iterable.iterator();
        while (iterator.hasNext()){
            Products products=iterator.next();
            int count=shopCarService.findCountByPid(products.getId());
            products.setShopcar_count(count);
        }
        //按照加入购物车量排序
        List<Products> productsList=new ArrayList<>();
        iterable.forEach(products -> productsList.add(products));
        Collections.sort(productsList, new Comparator<Products>() {
            @Override
            public int compare(Products o1, Products o2) {
                return o1.getShopcar_count()-o2.getShopcar_count();
            }
        });

        return new Result(false, "", iterable, map.get("token"));
    }

    //新建二级分类后的商品列表
    // https://www.cslapp.com/seller/pro/list_new?token=3&childtypeid=942
    @RequestMapping("/list_new")
    public Result list_new(@RequestParam Map<String, String> map) {
        Iterable<Products> iterable=productsService.list(map);
        return new Result(false, "", iterable, map.get("token"));
    }

    //  https://www.cslapp.com/seller/pro/list_webapp?token=1
    //微商城
    @RequestMapping("/list_webapp")
    public Result list_web(){

        Iterable<Products> iterable1=productsService.list_webapp(3);
        //Iterable<Products> iterable2=productsService.list_webapp(10);
        Iterable<Products> iterable2=productsService.list_webapp(9);
        List<Products> productsList=new ArrayList<>();
        iterable1.forEach(p->productsList.add(p));
        iterable2.forEach(p->productsList.add(p));
        Collections.sort(productsList, new Comparator<Products>() {
            @Override
            public int compare(Products o1, Products o2) {
                return o2.getId()-o1.getId();
            }
        });

        return new Result(false, Global.do_success, productsList, null);
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


    // http://localhost:8080/seller/pro/recommend?sellerId=3(&pid=商品ID)
    @RequestMapping("/recommend")
    public Result Recommend(String sellerId, String pid) {
        List<Products> list = productsService.recommend(sellerId, pid);
        return new Result(false, Global.do_success, list, null);
    }

    //  https://www.cslapp.com/seller/pro/findOne?pid=228
    @RequestMapping("/findOne")
    public Result findOne(Integer pid, String token, String pname, String ptype, String ptypep, String pcode) {
        Map<String, String> map = new HashMap<>();
        map.put("pname", pname);
        map.put("ptype", ptype);
        map.put("ptypep", ptypep);
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

    //  /seller/pro/save protypes参数追加值 =商品分类图片外链
    @RequestMapping("/save")
    @Transactional
    public Result update_all(Products products, String token, String protypes, String showHours, String killStart, String killEnd) throws FileNotFoundException, ParseException {

        //如果是更新的话，绑定原来的对应的折扣活动
        if(products.getId()!=null){
            Products old=productsService.findOne(products.getId());
            products.setActivityForDiscount(old.getActivityForDiscount());
        }

        //上线时间
        if (!StringUtils.isNullOrBlank(showHours)) {
            products.setShowDate(StringUtils.formatDate2(showHours));
        } else {
            products.setShowDate(new Date());
        }

        //秒杀
        if (products.getIssecondkill()) {
            products.setSecondKillStart(StringUtils.formatDate2(killStart));
            products.setSecondKillEnd(StringUtils.formatDate2(killEnd));
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
            String color = null;
            String size = null;
            String discount = null;
            String price = null;
            String secondKillPrice = "0";
            String amount = null;
            String pic = null;
            //商品上传
            if (strings.length == 6) {
                color = strings[0];//颜色
                size = strings[1];//尺码
                discount = "0.0";//折扣价
                price = strings[2];//原价
                secondKillPrice = strings[3];//秒杀价
                amount = strings[4];//库存
                pic = strings[5];//每个商品分类对应一个图片

            }
            //商品编辑
            if (strings.length == 7) {
                //兼容旧的宝贝上传接口
                color = strings[0];//颜色
                size = strings[1];//尺码
                discount = strings[2];//折扣价
                price = strings[3];//原价
                secondKillPrice = strings[4];//秒杀价
                amount = strings[5];//库存
                pic = strings[6];//每个商品分类对应一个图片
            }

            if (amount.equals("0")) {
                continue;
            }
            if (!StringUtils.isDiget(price)) {
                return new Result(true, Global.price_is_unright, null, null);
            }
            ProdutsType produtsType = new ProdutsType();
            produtsType.setPriceNew(Double.valueOf(price));
            produtsType.setColor(color);
            //原来有折扣价且折扣不为0
            if (discount!=null&&Double.valueOf(discount)!=0.0) {
                produtsType.setISDiscount(true);
                produtsType.setDiscountPrice(Double.valueOf(discount));
            }
            //原来没有折扣价或者折扣为0
            if(discount==null||Double.valueOf(discount)==0.0){
                produtsType.setDiscountPrice(0.0);
            }
            produtsType.setPic(pic);
            produtsType.setAmount(Integer.parseInt(amount));
            produtsType.setActive(true);
            produtsType.setPriceOld(0.0);
            produtsType.setSecondKillPrice(Double.valueOf(secondKillPrice));
            produtsType.setSize(size);
            produtsType.setSellerId(Integer.parseInt(token));
            produtsTypes.add(produtsType);
        }

        products.setProdutsTypes(produtsTypes);
        products.setSellerId(Integer.parseInt(token));
        products.setSellerName(sellerService.findOne(products.getSellerId()).getSellerName());

        try {
            //抽奖是否结束
            if (products.getId() != null && products.getIsLuckDraw() == false) {
                if (productsService.findOne(products.getId()).getIsLuckDraw()) {
                    products.setIsLuckDrawEnd(true);
                    LuckDraw luckDraw = luckDrawService.findBySellerId(products.getSellerId() + "");
                    luckDraw.setJoinNumber(0);
                    luckDrawService.update(luckDraw);
                    orderService.updateIsLuckDrawEnd(products.getId());
                }
            }
            productsService.save(products);
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
        if (StringUtils.isNullOrBlank(products.getQrcode())) {
            String url = productsService.qrcode(products.getSellerId() + "", products.getId() + "");
            products.setQrcode(Global.aliyun_href + url);
            productsService.update(products);
        }
        return new Result(false, Global.do_success, products, null);
    }

    //  https://www.cslapp.com/seller/pro/update_monthsale?pid=174&sellerId=3&monthSale=销量
    @RequestMapping("/update_monthsale")
    public Result updateMonthSale(int pid, int monthSale, int sellerId) {
        if (monthSale < 0) {
            return new Result(true, "销量" + Global.data_unright, null, null);
        }
        Products products = productsService.findOne(pid);
        products.setMonthSale(monthSale);
        productsService.update(products);
        return new Result(false, Global.do_success, products, null);
    }


    //  http://localhost:8080/seller/pro/get-secondPtypeNames?sellerId=4&ptypename=汽车贴膜
    @RequestMapping("/get-secondPtypeNames")
    public Result getSecondPtypeNames(String sellerId, String ptypename) {
        List<Products> list = productsService.findBySellerIdAndPtypeNameAndActive(sellerId, ptypename, true);
        List<String> secondPtypeNames = new ArrayList<>();
        if (list != null && list.size() != 0) {
            for (Products p : list) {
                if (!secondPtypeNames.contains(p.getSecondPtypeName())) {
                    secondPtypeNames.add(p.getSecondPtypeName());
                }
            }
        }
        return new Result(false, Global.do_success, secondPtypeNames, null);
    }


}
