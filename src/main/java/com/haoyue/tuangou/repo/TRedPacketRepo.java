package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TRedPacket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/6.
 */
public interface TRedPacketRepo extends TBaseRepo<TRedPacket,Integer> {
    List<TRedPacket> findByGroupCode(String groupcode);

    @Query(nativeQuery = true,value = "select * from t_redpacket where group_code=?1 and isowener=true")
    TRedPacket findByGroupCodeAndIsOwener(String groupCode);

    @Query(nativeQuery = true,value = "select * from t_redpacket where open_id=?1 and sale_id=?2 and isowener=true")
    List<TRedPacket> findByOpenIdAndIsOwener(String ownerOpenId, String saleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_redpacket set isover=true where group_code=?1")
    void updateIsoverByGroupCode(String groupCode);

    List<TRedPacket> findBySaleIdAndOpenId(String saleId, String openId);

    @Query(nativeQuery = true,value = "select * from t_redpacket where open_id=?2 and sale_id=?1 and isover=?3")
    List<TRedPacket> findBySaleIdAndOpenIdAndIsover(String saleId, String openId, Boolean isover);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_redpacket set isover=true where end_date<?1 and isover=false")
    void flushByDate(Date date);

    @Query(nativeQuery = true,value = "select wxname from  t_redpacket  where group_code=?1 ")
    List<String> findWxnameByGroupCode(String groupCode);
}
