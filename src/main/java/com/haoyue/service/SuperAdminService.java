package com.haoyue.service;

import com.haoyue.pojo.SuperAdmin;
import com.haoyue.repo.SuperAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/9/29.
 */
@Service
public class SuperAdminService {

    @Autowired
    private SuperAdminRepo superAdminRepo;

    public SuperAdmin login(SuperAdmin superAdmin) {
       return superAdminRepo.findByAdmin_nameAndAdmin_pass(superAdmin.getAdmin_name(),superAdmin.getAdmin_pass());
    }

    public SuperAdmin findOne(Integer id) {
        return superAdminRepo.findOne(id);
    }

    public SuperAdmin upadte(SuperAdmin superAdmin){
        return  superAdminRepo.save(superAdmin);
    }
}
