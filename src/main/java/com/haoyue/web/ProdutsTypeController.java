package com.haoyue.web;

import com.haoyue.pojo.ProdutsType;
import com.haoyue.pojo.ShopCar;
import com.haoyue.service.ProdutsTypeService;
import com.haoyue.service.ShopCarService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by LiJia on 2017/8/23.
 */
@RestController
@RequestMapping("/protype")
public class ProdutsTypeController {

    @Autowired
    private ProdutsTypeService produtsTypeService;
    @Autowired
    private ShopCarService shopCarService;

    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int pageSize) {
        return new Result(false, "", produtsTypeService.list(map, pageNumber, pageSize), map.get("token"));
    }

    @RequestMapping("/save")
    public Result save(ProdutsType produtsType, String token) {

        return new Result(produtsTypeService.save(produtsType), token);
    }

    @RequestMapping("/del")
    public Result del(Integer id, String token) {
        ProdutsType produtsType = produtsTypeService.findOne(id);
        if (produtsType.getSellerId() != Integer.parseInt(token)) {
            return new Result(true, Global.have_no_right, token);
        }
        produtsTypeService.del(produtsType);
        return new Result(false, Global.do_success, token);
    }

    //   /protype/update?id=商品分类ID&discountPrice=最终的折扣价&token=店铺ID
    @RequestMapping("/update")
    public Result update(ProdutsType produtsType, String token){
        ProdutsType old=produtsTypeService.findOne(produtsType.getId());
        if (old.getSellerId()!=Integer.parseInt(token)){
            return new Result(true, Global.have_no_right,null,token);
        }
        if (produtsType.getAmount()!=null){
            old.setAmount(produtsType.getAmount());
        }
        if (produtsType.getPriceNew()!=null){
            old.setPriceOld(old.getPriceNew());
            old.setPriceNew(produtsType.getPriceNew());
        }
        if(produtsType.getDiscountPrice()!=null){
            old.setDiscountPrice(produtsType.getDiscountPrice());
            //降价通知
            shopCarService.sendCustomerWxTemplate(old.getId(), old.getSellerId());
        }
        produtsTypeService.save(old);
        return new Result(false, Global.do_success,null,token);
    }

}
