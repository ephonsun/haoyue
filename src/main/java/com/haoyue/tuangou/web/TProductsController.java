package com.haoyue.tuangou.web;

import com.haoyue.Exception.MyException;
import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.service.*;
import com.haoyue.tuangou.utils.*;
import com.haoyue.tuangou.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by LiJia on 2017/11/7.
 */
@RestController
@RequestMapping("/tuan/product")
public class TProductsController {

    @Autowired
    private TProductsService tProductsService;
    @Autowired
    private TUserSaleService tUserSaleService;
    @Autowired
    private TProductsTypesService tProductsTypesService;
    @Autowired
    private TProductsTypesNameService tProductsTypesNameService;
    @Autowired
    private TuanOrdersService tuanOrdersService;

    /**
     * @param tProducts
     * @param tprotypes
     * @return /tuan/product/save?saleId=1&pname=商品名&style=款号&types=分类&indexPic=主图&detailPic=详情图
     * &parameters=商品参数&deliver=快递模板ID或者null&tuanNumbers=拼团人数&tuanTimes=拼团时间&isFree=true/fasle
     * &tprotypes=黄色,M,100,88,66,99=黑色,L,100,88,66,99&isFree=true/false&suffix=价格后缀
     */
    @RequestMapping("/save")
    @Transactional
    public TResult save(TProducts tProducts, String tprotypes,String start_date,String end_date) {
        //拼团时间范围
        if (!StringUtils.isNullOrBlank(start_date)) {
            try {
                Date endDate = StringUtils.formatStrToDate2(end_date);
                Date startDate = StringUtils.formatStrToDate2(start_date);
                tProducts.setStartDate(startDate);
                tProducts.setEndDate(endDate);
            } catch (ParseException e) {
                return new TResult(true, TGlobal.date_format_wrong, null);
            }
        }

        //先保存商品
        Date date = new Date();
        if (tProducts.getId() == null) {
            tProducts.setCreateDate(date);
        } else {
            if (!tProductsService.findOne(tProducts.getId()).getSaleId().equals(tProducts.getSaleId())) {
                return new TResult(true, TGlobal.have_no_right, null);
            }
            //清空之前关联的商品分类
            tProductsService.delPtypes(tProducts.getId());
        }

        //是否团购
        if (tProducts.getTuanNumbers() != 0 && tProducts.getTuanTimes() != 0) {
            tProducts.setIsTuan(true);
        }
        tProductsService.save(tProducts);

        //再保存商品详细分类
        if (!StringUtils.isNullOrBlank(tprotypes)) {
            String[] lines = tprotypes.split("=");
            String color = "";
            String size = "";
            String oldprice = "";
            String newprice = "";
            String tuanprice = "";
            String amount = "";
            List<TProductsTypes> list = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                String line[] = lines[i].split(",");
                color = line[0];
                size = line[1];
                oldprice = line[2];
                newprice = line[3];
                tuanprice = line[4];
                amount = line[5];
                if (amount.equals("0")) {
                    continue;
                }
                if (StringUtils.isNullOrBlank(color) || StringUtils.isNullOrBlank(size) || StringUtils.isNullOrBlank(oldprice) || StringUtils.isNullOrBlank(newprice) || StringUtils.isNullOrBlank(tuanprice) || StringUtils.isNullOrBlank(amount)) {
                    return new TResult(true, TGlobal.pro_isNull_blank, null);
                }
                if (!StringUtils.isDiget(oldprice) || !StringUtils.isDiget(newprice) || !StringUtils.isDiget(tuanprice) || !StringUtils.isDiget(amount)) {
                    return new TResult(true, TGlobal.pro_notdiget, null);
                }
                TProductsTypes tProductsTypes = new TProductsTypes();
                tProductsTypes.setCreateDate(date);
                tProductsTypes.setSaleId(tProducts.getSaleId());
                tProductsTypes.setAmount(Integer.parseInt(amount));
                tProductsTypes.setColor(color);
                tProductsTypes.setNewPrice(Double.valueOf(newprice));
                tProductsTypes.setOldPrice(Double.valueOf(oldprice));
                tProductsTypes.setSize(size);
                tProductsTypes.setTuanPrice(Double.valueOf(tuanprice));
                tProductsTypesService.save(tProductsTypes);
                list.add(tProductsTypes);
            }
            //商品=商品分类
            tProducts.setProductsTypes(list);
            TProducts products= tProductsService.update(tProducts);
            //保存---设置二维码
//            if (StringUtils.isNullOrBlank(products.getQrcode())){
//                try {
//                    String qrcode_url=tProductsService.qrcode(products.getSaleId(),products.getId()+"");
//                    products.setQrcode(TGlobal.aliyun_href+qrcode_url);
//                    tProductsService.update(products);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
            //更新商品分类名称
            tProductsTypesNameService.update(tProducts.getSaleId(), tProducts.getTypes());

        }
        return new TResult(false, TGlobal.do_success, null);
    }

    // https://www.cslapp.com/tuan/product/uploadPic?saleId=1&files=121211221
    // http://localhost:8080/tuan/product/uploadPic?saleId=1&files=121211221
    // /tuan/product/uploadPic?id=卖家ID&files=需要上传的所有图片
    @RequestMapping("/uploadPic")
    public Object uploadFile(MultipartFile[] file, TUserSale sale, String saleId) {
        sale.setId(Integer.parseInt(saleId));
        Iterable<TUserSale> iterable = tUserSaleService.findOne(sale);
        Iterator<TUserSale> iterator = iterable.iterator();
        TUserSale tUserSale = iterator.next();
        int size_kb = 0;
        String url = "";
        StringBuffer stringBuffer = new StringBuffer();
        // 循环获得每个文件
        if (file != null && file.length != 0) {
            for (int i = 0; i < file.length; i++) {
                MultipartFile multipartFile = file[i];
                //校验存储空间是否够用
                size_kb = (int) multipartFile.getSize() / 1024;
                if ((size_kb + tUserSale.getUploadFile()) >= tUserSale.getMaxFile()) {
                    return new TResult(true, TGlobal.space_not_enough, null);
                } else {
                    tUserSale.setUploadFile(tUserSale.getUploadFile() + size_kb);
                }
                //上传图片
                TOSSClientUtil tossClientUtil = new TOSSClientUtil();
                try {
                    //返回上传的图片地址
                    url = tossClientUtil.uploadImg2Oss(multipartFile);
                } catch (MyException e) {
                    e.printStackTrace();
                }
                //拼接返回的图片地址
                url = TGlobal.aliyun_href + url;
                stringBuffer.append(url);
                if (i != file.length - 1) {
                    stringBuffer.append(",");
                }
            }
        }
        //更新sale的存储空间
        tUserSaleService.update(tUserSale);
        if (file == null || file.length == 0) {
            System.out.println("files为空");
        }
        System.out.println(stringBuffer.toString());
        return new TUploadRepo(stringBuffer.toString());
    }

    // /tuan/product/list?saleId=1&active=true/false
    @RequestMapping("/list")
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<TProducts> iterable = tProductsService.list(map, pageNumber, pageSize);
        Iterator<TProducts> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            TProducts products = iterator.next();
            List<TProductsTypes> productsTypes = products.getProductsTypes();
            List<TProductsTypes> list = new ArrayList<>();
            for (TProductsTypes types : productsTypes) {
                if (types.getIsActive()) {
                    list.add(types);
                }
            }
            products.setProductsTypes(list);
        }
        return new TResult(false, TGlobal.do_success, iterable);
    }

    //   零元购商品列表  /tuan/product/free_list?saleId=123
    @RequestMapping("/free_list")
    public TResult freeProducts(@RequestParam Map<String, String> map){

        Iterable<TProducts> iterable =tProductsService.freeproducts(map);
        Iterator<TProducts> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            TProducts products = iterator.next();
            List<TProductsTypes> productsTypes = products.getProductsTypes();
            List<TProductsTypes> list = new ArrayList<>();
            for (TProductsTypes types : productsTypes) {
                if (types.getIsActive()) {
                    list.add(types);
                }
            }
            products.setProductsTypes(list);
        }
        return new TResult(false, TGlobal.do_success, iterable);
    }


    // /tuan/product/update?id=商品Id&active=true/false
    // http://localhost:8080/tuan/product/update?id=1&active=false
    @RequestMapping("/update")
    public TResult update(TProducts tProducts) {
        TProducts products = tProductsService.findOne(tProducts.getId());
        if (!tProducts.getSaleId().equals(products.getSaleId())) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        //下架、上架
        products.setActive(tProducts.getActive());
        tProductsService.update(products);

        TProductsTypesName typesName = tProductsTypesNameService.findBySaleId(products.getSaleId());
        String typename = products.getTypes();
        String old = typesName.getTypes();
        String names[]=old.split(",");
        String news="";
        //下架更新商品分类名称
        if (!products.getActive()) {
            List<TProducts> productsList = tProductsService.findByTypesAndSaleId(typename, products.getSaleId());
            //当前分类只对应一个商品
            if (productsList == null||productsList.size()==0) {
                for (int i=0;i<names.length;i++){
                    if (names[i].equals(typename)){
                        names[i]="";
                    }
                    if (!StringUtils.isNullOrBlank(names[i])){
                        news+=","+names[i];
                    }
                }
                typesName.setTypes(news.substring(1));
            }
        }
        //上架更新商品分类名称
        if (products.getActive()){
            boolean flag=false;
            for (String name:names){
                if (name.equals(typename)){
                    flag=true;
                }
            }
            if (!flag){
                typesName.setTypes(typesName.getTypes()+","+typename);
            }
        }
        tProductsTypesNameService.update2(typesName);
        return new TResult(false, TGlobal.do_success, null);
    }


    // /tuan/product/findone?pid=商品Id&active=true/false
    @RequestMapping("/findone")
    public TResult findOne(String pid, String active, String saleId) {
        TProducts tProducts = tProductsService.findOne(Integer.parseInt(pid));
        if (active.equals("true")) {
            List<TProductsTypes> newlist = new ArrayList<>();
            List<TProductsTypes> list = tProducts.getProductsTypes();
            for (TProductsTypes type : list) {
                if (type.getIsActive()) {
                    newlist.add(type);
                }
            }
            tProducts.setProductsTypes(newlist);
        }
        return new TResult(false, TGlobal.do_success, tProducts);
    }


    // /tuan/product/pname?saleId=12&pname=商品名
    @RequestMapping("/pname")
    public TResult findByName(String pname, String saleId) {
        if (StringUtils.isNullOrBlank(pname)) {
            return new TResult(true, TGlobal.pro_name_null, null);
        }
        Iterable<TProducts> iterable = tProductsService.findByPname(pname, saleId);
        return new TResult(false, TGlobal.do_success, iterable);
    }

    // /tuan/product/news?saleId=12
    @RequestMapping("/news")
    public TResult news(String saleId) {
        Iterable<TProducts> iterable = tProductsService.news(saleId);
        return new TResult(false, TGlobal.do_success, iterable);
    }

    // /tuan/product/alls?saleId=12&typeName=分类名
    @RequestMapping("/alls")
    public TResult alls(String saleId, String typeName) {
        Iterable<TProducts> iterable = tProductsService.alls(saleId, typeName);
        return new TResult(false, TGlobal.do_success, iterable);
    }

    // /tuan/product/tuanorders?pid=商品Id&saleId=12
    @RequestMapping("/tuanorders")
    public TResult findTuanOrdersByPid(Integer pid, String saleId) {
        Iterable<TuanOrders> iterable = tuanOrdersService.findTuanOrdersByTProducts(pid, saleId, null);
        Iterator<TuanOrders> iterator = iterable.iterator();
        List<TuanOrders> ownerList = new ArrayList<>();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            TuanOrders tuanOrders = iterator.next();
            if (tuanOrders.getIsowner()) {
                ownerList.add(tuanOrders);
            }
        }
        TProductsTuanResponse response = new TProductsTuanResponse();
        response.setAmount(count);
        response.setTuanOrdersList(ownerList);
        return new TResult(false, TGlobal.do_success, response);
    }

    // /tuan/product/recommend?pid=当前查询的商品Id&saleId=12
    @RequestMapping("/recommend")
    public TResult recommend(String saleId, String pid) {
        List<TProducts> result = new ArrayList<>();
        if (!StringUtils.isNullOrBlank(pid)) {
            result = tProductsService.recommend(saleId, pid);
        } else {
            result = tProductsService.recommend2(saleId);
        }
        return new TResult(false, TGlobal.do_success, result);
    }

    // http://localhost:8080/tuan/product/test?pid=1&saleId=1
    @RequestMapping("/test")
    public void test(String pid) throws FileNotFoundException {
       String result= tProductsService.qrcode("1",pid);
       System.out.println(TGlobal.aliyun_href+result);
    }
}
