package com.haoyue.repo;

import com.haoyue.pojo.ProdutsType;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/8/23.
 */
public interface ProdutsTypeRepo extends BaseRepo<ProdutsType,Integer> {


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from protypes where product_id =?1")
    void deleteByProId(Integer id);
}
