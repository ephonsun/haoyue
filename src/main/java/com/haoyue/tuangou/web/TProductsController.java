package com.haoyue.tuangou.web;

import com.haoyue.tuangou.service.TProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/11/7.
 */
@RestController
public class TProductsController {

    @Autowired
    private TProductsService tProductsService;

    public void save(){

    }

}
