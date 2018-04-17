package com.haoyue.service;

import com.haoyue.pojo.CustomeCard;
import com.haoyue.repo.CustomeCardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lijia on 2018/4/17.
 */
@Service
public class CustomeCardService {

    @Autowired
    private CustomeCardRepo customeCardRepo;

    public void save(CustomeCard customeCard) {
        customeCardRepo.save(customeCard);
    }
}
