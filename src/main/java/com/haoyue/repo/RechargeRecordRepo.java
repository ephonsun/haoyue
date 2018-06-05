package com.haoyue.repo;

import com.haoyue.pojo.RechargeRecord;

/**
 * Created by Lijia on 2018/6/4.
 */
public interface RechargeRecordRepo extends BaseRepo<RechargeRecord,Integer> {
    RechargeRecord findByOrdercode(String out_trade_no);
}
