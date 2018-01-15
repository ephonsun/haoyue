package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TDeliverTemplates;

import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 */
public interface TDeliverTemplatesRepo extends  TBaseRepo<TDeliverTemplates,Integer> {
    List<TDeliverTemplates> findBySaleId(String saleId);

    TDeliverTemplates findBySaleIdAndDname(String saleId, String dname);
}
