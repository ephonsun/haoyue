package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TRedPacket;
import com.haoyue.tuangou.repo.TRedPacketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/6.
 */
@Service
public class TRedPacketService {

    @Autowired
    private TRedPacketRepo tRedPacketRepo;

    public List<TRedPacket> findByGroupCode(String groupcode) {
        return tRedPacketRepo.findByGroupCode(groupcode);
    }

    public TRedPacket findByGroupCodeAndIsOwener(String groupCode) {
       return tRedPacketRepo.findByGroupCodeAndIsOwener(groupCode);
    }

    public void save(TRedPacket tRedPacket) {
        tRedPacketRepo.save(tRedPacket);
    }

    public List<TRedPacket> findByOpenIdAndIsOwener(String ownerOpenId,String saleId) {
        return tRedPacketRepo.findByOpenIdAndIsOwener(ownerOpenId,saleId);
    }

    public void updateIsoverByGroupCode(String groupCode) {
        tRedPacketRepo.updateIsoverByGroupCode(groupCode);
    }

    public List<TRedPacket> findBySaleIdAndOpenId(String saleId, String openId) {
        return tRedPacketRepo.findBySaleIdAndOpenId(saleId,openId);
    }

    public List<TRedPacket> findBySaleIdAndOpenIdAndIsover(String saleId, String openId, Boolean isover) {
        return tRedPacketRepo.findBySaleIdAndOpenIdAndIsover(saleId,openId,isover);
    }

    public void flush() {
        Date date=new Date();
        tRedPacketRepo.flushByDate(date);
    }

    public List<String> findWxnameByGroupCode(String groupCode) {
        return tRedPacketRepo.findWxnameByGroupCode(groupCode);
    }
}
