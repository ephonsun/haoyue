package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TAddress;
import com.haoyue.tuangou.repo.TAddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiJia on 2017/11/13.
 */
@Service
public class TAddressService {

    @Autowired
    private TAddressRepo addressRepo;

    public List<TAddress> findByOpenIdAndSaleId(String openId, String saleId) {
        List<TAddress> address=addressRepo.findByOpenIdAndSaleId(openId,saleId);
        return address;
    }

    public void save(TAddress tAddress) {
        addressRepo.save(tAddress);
    }
}
