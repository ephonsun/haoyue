package com.haoyue.service;

import com.haoyue.pojo.Dictionary;
import com.haoyue.pojo.QSeller;
import com.haoyue.pojo.Seller;
import com.haoyue.repo.SellerRepo;
import com.haoyue.untils.Global;
import com.haoyue.Exception.MyException;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/21.
 */
@Service("SellerService")
public class SellerService {
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private DictionaryService dictionaryService;
    public Seller login(Seller seller) {



        //判断用户名、邮箱、手机号登录
        Seller seller1 = sellerRepo.findBySellerNameAndSellerPass(seller.getSellerName(), seller.getSellerPass());
        if (seller1 == null) {
            seller1 = sellerRepo.findBySellerPassAndSellerEmail(seller.getSellerPass(), seller.getSellerName());
        } else {
            return seller1;
        }
        if (seller1 == null) {
            seller1 = sellerRepo.findBySellerPassAndSellerPhone(seller.getSellerPass(), seller.getSellerName());
        }

        return seller1;
    }

    public Seller findOne(Integer id) {
        return sellerRepo.findOne(id);
    }

    public Result save(Seller seller) throws MyException {
        try {
            //判断权限格式，APPID是否准确
            if (StringUtils.isNullOrBlank(seller.getAuthority()) || StringUtils.isNullOrBlank(seller.getAppId())) {
                return new Result(true, Global.appid_authority_isnull, null, null);
            }
            for (int i = 0; i < seller.getAuthority().length(); i++) {
                if (!Character.isDigit(seller.getAuthority().charAt(i))) {
                    return new Result(true, Global.authority_not_digit, null, null);
                }
            }
            //判断appid是否已经存在
            List<Seller> sellerList = sellerRepo.findByAppId(seller.getAppId());
            if (sellerList != null && sellerList.size() >= 1) {
                return new Result(true, Global.do_success, Global.appid_exist, null);
            }
            //判断权限值，初始化存储空间
            int i = 1;
            if (seller.getAuthority().equals("0")) {
                i = 1;
            } else if (seller.getAuthority().equals("1")) {
                i = 5;
            } else if (seller.getAuthority().equals("2")) {
                i = 10;
            }
            seller.setMaxFileSize(Global.max_file_size * i);
            seller.setUploadFileSize(0);
            seller.setExpireDate(StringUtils.addAllYear());
            sellerRepo.save(seller);
            dictionaryService.save(new Dictionary(), seller.getSellerId() + "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(Global.do_fail + e.getMessage());
        }
        return new Result(false, Global.do_success, seller, null);
    }

    public Iterable<Seller> filter(Map<String, String> map) {

        QSeller seller = QSeller.seller;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("sellerName")) {
                    bd.and(seller.sellerName.eq(value));
                }
                if (name.equals("sellerPhone")) {
                    bd.and(seller.sellerPhone.eq(value));
                }
                if (name.equals("sellerEmail")) {
                    bd.and(seller.sellerEmail.eq(value));
                }
                if (name.equals("token")) {
                    bd.and(seller.sellerId.eq(Integer.parseInt(value)));
                }
            }
        }
        return sellerRepo.findAll(bd.getValue());
    }

    public Iterable<Seller> list(Map<String, String> map, int pageNumber, int pageSzie) {

        QSeller seller = QSeller.seller;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

                if (name.equals("sellerName")) {
                    bd.and(seller.sellerName.eq(value));
                }

                if (name.equals("sellerPhone")) {
                    bd.and(seller.sellerPhone.eq(value));
                }

                if (name.equals("sellerEmail")) {
                    bd.and(seller.sellerEmail.eq(value));
                }
                if (name.equals("token")) {
                    bd.and(seller.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("authority")) {
                    bd.and(seller.authority.eq(value));
                }
            }
        }

        return sellerRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSzie));
    }


    public void update(Seller seller) {
        Seller seller1 = findOne(seller.getSellerId());
        if (!StringUtils.isNullOrBlank(seller.getSellerEmail())) {
            seller1.setSellerEmail(seller.getSellerEmail());
        }
        if (!StringUtils.isNullOrBlank(seller.getBanners())) {
            seller1.setBanners(seller.getBanners());
        }
        if (!StringUtils.isNullOrBlank(seller.getSellerPhone())) {
            seller1.setSellerPhone(seller.getSellerPhone());
        }
        if (!StringUtils.isNullOrBlank(seller.getSellerPass())) {
            seller1.setSellerPass(seller.getSellerPass());
        }
        if (!StringUtils.isNullOrBlank(seller.getUploadFileSize() + "")) {
            seller1.setUploadFileSize(seller.getUploadFileSize());
        }
        if (!StringUtils.isNullOrBlank(seller.getVideos())) {
            seller1.setVideos(seller.getVideos());
        }
        if (!StringUtils.isNullOrBlank(seller.getSellerName())) {
            seller1.setSellerName(seller.getSellerName());
        }
        if (!StringUtils.isNullOrBlank(seller.getLunbo())) {
            seller1.setLunbo(seller.getLunbo());
        }
        sellerRepo.save(seller1);
    }

    public void update2(Seller seller) {
        sellerRepo.save(seller);
    }

    public Seller findBySellerPhone(String phone) {
        return sellerRepo.findBySellerPhone(phone);
    }

    public boolean isStop(String sellerId) {
        Seller seller = sellerRepo.findOne(Integer.parseInt(sellerId));
        if (seller.getExpireDate().before(new Date())) {
            seller.setIsActive(false);
            update2(seller);
        }
        if (seller.getIsActive() == false) {
            return true;
        }
        return false;
    }

    public boolean findByOnlineCode(String online_code) {
        List<String> strs = sellerRepo.findByOnlineCode(online_code);
        if (strs != null && strs.size() > 0) {
            return true;
        }
        return false;
    }

    public Seller findOneById(int i) {
        return findOne(i);
    }
}

