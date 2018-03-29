package com.haoyue.repo;

import com.haoyue.pojo.MemberGroup;

import java.util.List;

/**
 * Created by Lijia on 2018/3/13.
 */
public interface MemberGroupRepo extends BaseRepo<MemberGroup,Integer> {
    List<MemberGroup> findBySellerId(String sellerId);
}
