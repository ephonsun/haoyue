package com.haoyue.hywebsite;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2018/1/12.
 * 游客
 */

@RestController
@RequestMapping("/website/tourist")
public class TouristController {

    @Autowired
    private TouristService touristService;

    //  /website/tourist/regist?username=用户名&password=密码&phone=手机&sex=男/女&address=省#市#县&message=需求
    @RequestMapping("/regist")
    public Result regist(Tourist tourist) {
        tourist.setCreateDate(new Date());
        if (touristService.findByPhone(tourist.getPhone())!=null){
            return new Result(true, "当前手机号已存在", null);
        }
        if (touristService.findByUsername(tourist.getUsername())!=null){
            return new Result(true, "当前用户名已经存在", null);
        }
        touristService.save(tourist);
        return new Result(false, "success", tourist);
    }

    //  /website/tourist/login?username=1111&password=111
    @RequestMapping("/login")
    public Result login(String username, String password) {
        Tourist tourist = touristService.findByUsernameAndPassword(username, password);
        if (tourist == null) {
            return new Result(true, "用户名或密码错误", tourist);
        }
        return new Result(false, "success", tourist);
    }

    //  /website/tourist/list?pageNumber=0
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Tourist> iterable = touristService.list(map, pageNumber, pageSize);
        return new Result(false, "success", iterable);
    }

    //  https://www.cslapp.com/website/tourist/findone?id=123
    @RequestMapping("/findone")
    public Result findOne(int id, String phone, String username) {
        if (id != 0) {
            Tourist tourist = touristService.findOne(id);
            return new Result(false, "success", tourist);
        }
        if (phone != null && !phone.equals("")) {
            Tourist tourist = touristService.findByPhone(phone);
            if (tourist != null) {
                return new Result(true, "当前手机号已存在", null);
            }
        }
        if (username != null && !username.equals("")) {
            Tourist tourist = touristService.findByUsername(username);
            if (tourist != null) {
                return new Result(true, "当前用户名已经存在", null);
            }
        }
        return new Result(false, "success", null);
    }

    //  /website/tourist/del?id=123
    @RequestMapping("/del")
    public Result del(int id) {
        touristService.del(id);
        return new Result(false, "success", null);
    }

    //  /website/tourist/phonecode?phone=手机号  返回验证码，再和用户输入的验证码对比
    @RequestMapping("/phonecode")
    public Result getPhoneCode(String phone) {
        phone = phone.trim();
        //校验手机号码
        if (phone == null || phone.equals("")) {
            return new Result(true, "手机号不能为空", null);
        }
        if (phone.length() != 11) {
            return new Result(true, "手机号应该为11位数字", null);
        }
        for (char i : phone.toCharArray()) {
            if (!Character.isDigit(i)) {
                return new Result(true, "手机号应该为11位数字", null);
            }
        }
        String code = getCode();
        try {
            //发送验证码
            SendCode.sendSms(phone, code);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new Result(false, "success", code);
    }


    public String getCode() {
        String str = "0123456789";
        int index = 0;
        String code = "";
        for (int i = 0; i < 4; i++) {
            index = (int) (Math.random() * str.length());
            code += str.charAt(index);
        }
        return code;
    }

}
