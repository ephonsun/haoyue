package com.haoyue.service;

import com.haoyue.pojo.Address;
import com.haoyue.pojo.Customer;
import com.haoyue.repo.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiJia on 2017/9/5.
 */
@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private CustomerService customerService;

    public void save(Address address, String openId,String sellerId) {
        address.setOpenId(openId);
        addressRepo.save(address);
        Customer customer=customerService.findByOpenId(openId,sellerId);
        List<Address> olds=customer.getAddressList();
        List<Address> news=new ArrayList<>();
        news.addAll(olds);
        news.add(address);
        customer.setAddressList(news);
        customerService.update(customer);
    }

    public void save2(Address address){
        addressRepo.save(address);
    }

    public Address findOne(int id) {
        return addressRepo.findOne(id);
    }

    public void update(Address address) {
        addressRepo.save(address);
    }
}
