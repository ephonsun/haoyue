package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TCollections;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/11/8.
 */
public interface TCollectionsRepo extends TBaseRepo<TCollections,Integer> {
    TCollections findByCidAndSaleId(String cid, String saleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete  from t_collections_productses where productses_id=?2 and tcollections_id=?1")
    void delByCollectionIdAndPid(Integer cid, Integer pid);
}
