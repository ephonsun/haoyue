package com.tuangou.repo;

import com.haoyue.repo.BaseRepo;
import com.tuangou.pojo.TUserSale;

/**
 * Created by LiJia on 2017/11/2.
 */
public interface TUserSaleRepo extends BaseRepo<TUserSale,Integer> {
    TUserSale findByNameAndPass(String name_phone_email, String pass);

    TUserSale findByPhoneAndPass(String name_phone_email, String pass);

    TUserSale findByEmailAndPass(String name_phone_email, String pass);
}
