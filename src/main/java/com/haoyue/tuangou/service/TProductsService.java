package com.haoyue.tuangou.service;


import com.haoyue.tuangou.repo.TProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/7.
 */
@Service
public class TProductsService {

    @Autowired
    private TProductsRepo tProductsRepo;

}
