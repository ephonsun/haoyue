package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TUserSale;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LiJia on 2017/11/2.
 */
public interface TUserSaleRepo extends TBaseRepo<TUserSale,Integer> {
    TUserSale findByNameAndPass(String name_phone_email, String pass);

    TUserSale findByPhoneAndPass(String name_phone_email, String pass);

    TUserSale findByEmailAndPass(String name_phone_email, String pass);

    @Query(nativeQuery = true,value = "select id from t_usersale")
    List<Integer> findAllIds();

    List<TUserSale> findByOnlineCode(String onlinecode);
}
