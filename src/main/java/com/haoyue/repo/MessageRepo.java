package com.haoyue.repo;

import com.haoyue.pojo.Message;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */
public interface MessageRepo extends BaseRepo<Message,Integer> {

    @Query(nativeQuery = true,value = "select * from message where after_sale_id=?1")
    List<Message> findByAfter_sale_id(int afterSaleId);
}
