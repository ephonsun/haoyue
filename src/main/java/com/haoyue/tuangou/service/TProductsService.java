package com.haoyue.tuangou.service;


import com.haoyue.tuangou.pojo.QTProducts;
import com.haoyue.tuangou.pojo.TProducts;
import com.haoyue.tuangou.repo.TProductsRepo;
import com.haoyue.tuangou.utils.QRcode;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TOSSClientUtil;
import com.haoyue.tuangou.wxpay.HttpRequest;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/7.
 */
@Service
public class TProductsService {

    @Autowired
    private TProductsRepo tProductsRepo;
    @Autowired
    private TuanOrdersService tuanOrdersService;

    public void save(TProducts tProducts) {
        tProductsRepo.save(tProducts);
    }

    public TProducts update(TProducts tProducts) {
        return tProductsRepo.save(tProducts);
    }

    public Iterable<TProducts> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTProducts products = QTProducts.tProducts;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("saleId")) {
                    bd.and(products.saleId.eq(value));
                } else if (name.equals("active")) {
                    bd.and(products.active.eq(Boolean.valueOf(value)));
                } else if (name.equals("pname")) {
                    value = value.trim();
                    bd.and(products.pname.contains(value));
                } else if (name.equals("isfree")) {
                    bd.and(products.isFree.eq(true));
                }

            }
        }
        return tProductsRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }

    public TProducts findOne(Integer id) {
        return tProductsRepo.findOne(id);
    }

    public List<TProducts> findByTuanProduct(String saleId) {
        return tProductsRepo.findByTuanProduct(saleId);
    }

    public Iterable<TProducts> findByPname(String pname, String saleId) {
        QTProducts products = QTProducts.tProducts;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(products.saleId.eq(saleId));
        bd.and(products.active.eq(true));
        bd.and(products.pname.contains(pname));
        return tProductsRepo.findAll(bd.getValue());
    }

    public Iterable<TProducts> index(Integer saleId) {
        QTProducts products = QTProducts.tProducts;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(saleId)));
        bd.and(products.active.eq(true));
        return tProductsRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "saleNum"));
    }

    public Iterable<TProducts> news(String saleId) {
        QTProducts products = QTProducts.tProducts;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(saleId)));
        bd.and(products.active.eq(true));
        return tProductsRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TProducts> alls(String saleId, String typeName) {
        QTProducts products = QTProducts.tProducts;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(saleId)));
        bd.and(products.active.eq(true));
        if (!StringUtils.isNullOrBlank(typeName)) {
            bd.and(products.types.eq(typeName));
        }
        return tProductsRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "saleNum"));
    }

    public List<TProducts> recommend(String saleId, String pid) {
        List<Integer> ids = findPidsBySaleId(saleId);
        List<TProducts> result = new ArrayList<>();
        List<Integer> newids = new ArrayList<>();
        //如果只有一个商品，则推荐为null
        if (ids.size() == 1) {
            return null;
        }
        //如果四个商品，推荐剩余的商品
        if (ids.size() <= 4) {
            for (Integer id : ids) {
                if (id == Integer.parseInt(pid)) {
                    continue;
                }
                result.add(findOne(id));
            }
            return result;
        }
        //如果多余四个商品，推荐剩余商品中的三个
        for (int i = 0; i < 3; i++) {
            int index = (int) Math.floor(Math.random() * ids.size());
            int id = ids.get(index);
            while (newids.contains(id) || id == Integer.parseInt(pid)) {
                index = (int) Math.floor(Math.random() * ids.size());
                id = ids.get(index);
            }
            newids.add(id);
            result.add(findOne(id));
        }
        return result;
    }

    private List<Integer> findPidsBySaleId(String saleId) {
        return tProductsRepo.findPidsBySaleId(saleId);
    }

    public List<TProducts> recommend2(String saleId) {
        List<Integer> ids = findPidsBySaleId(saleId);
        List<TProducts> result = new ArrayList<>();
        List<Integer> newids = new ArrayList<>();
        if (ids.size() <= 3) {
            for (Integer id : ids) {
                result.add(findOne(id));
            }
            return result;
        }
        for (int i = 0; i < 3; i++) {
            int index = (int) Math.floor(Math.random() * ids.size());
            int id = ids.get(index);
            while (newids.contains(id)) {
                index = (int) Math.floor(Math.random() * ids.size());
                id = ids.get(index);
            }
            newids.add(id);
            result.add(findOne(id));
        }
        return result;
    }

    public void delPtypes(Integer pid) {
        tProductsRepo.delPtypes(pid);
    }

    public List<TProducts> findByTypesAndSaleId(String typename, String saleId) {
        return tProductsRepo.findByTypesAndSaleId(typename, saleId);
    }

    public void autoFlushEnd() {
        List<TProducts> list = tProductsRepo.findByEndDateAndIsEnd(new Date());
        if (list != null && list.size() != 0) {
            for (TProducts products : list) {
                //刷新 isend 属性
                tProductsRepo.updateEnd(products.getId());
                //刷新团购订单状态
                tuanOrdersService.updateEndByPid(products.getId());
            }
        }
    }

    public Iterable<TProducts> freeproducts(Map<String, String> map) {
        QTProducts products = QTProducts.tProducts;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(products.saleId.eq(String.valueOf(map.get("saleId"))));
        bd.and(products.active.eq(true));
        bd.and(products.isFree.eq(true));
        return tProductsRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    //生成二维码
    public String qrcode(String saleId, String pid) throws FileNotFoundException {

        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);

        // d:/haoyue/erweima/1.jpg
        String filename = QRcode.getminiqrQr(access_token, pid);
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        TOSSClientUtil tossClientUtil = new TOSSClientUtil();
        // hymarket/qrcode/xx.jpg
        filename = "qrcodes/tuan-" + pid + ".jpg";
        tossClientUtil.uploadFile2OSS(fileInputStream, filename, null);
        //删除旧的数据
        file.delete();
        return filename;
    }


}
