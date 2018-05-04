package com.haoyue.repo;

import com.haoyue.pojo.Products;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Created by LiJia on 2017/8/22.
 */

public interface ProductsRepo extends BaseRepo<Products,Integer> {
    Products findByPcode(String pcode);

    @Query(nativeQuery = true,value = "select distinct(ptype_name) from products where seller_id=?1 and active=true")
    List<String> findBySellerIdAndActive(Integer sellerId);

    @Query(nativeQuery = true,value = "select * from products where seller_id=?1 and create_date<?2")
    List<Products> findBySellerIdAndCreateDate(String sellerId, Date date);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update products set issecondkill=false where issecondkill=true and  second_kill_end<?1")
    void autoFlush(Date date);

    @Query(nativeQuery = true,value = "select * from products where seller_id=?1 and active=?2")
    List<Products> findBySellerIdAndActive(String sellerId, boolean flag);

    List<Products> findBySellerIdAndPtypeNameAndActive(Integer sellerId, String ptypename, boolean b);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from products_childtypes where childtypes_id=?1")
    void del_childtypes_middle(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from products_parenttypes where parenttypes_id=?1")
    void del_parenttypes_middle(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from products_parenttypes where parenttypes_id=?1 and products_id=?2")
    void unbind_parenttypes_middle(Integer id,Integer pid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from products_childtypes where childtypes_id=?1 and products_id=?2")
    void unbind_childtypes_middle(Integer id ,Integer pid);

}
