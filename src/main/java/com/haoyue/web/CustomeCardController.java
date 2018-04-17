package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.CustomeCard;
import com.haoyue.service.CustomeCardService;
import com.haoyue.untils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Lijia on 2018/4/17.
 */
@RestController
@RequestMapping("/customecard")
public class CustomeCardController {


    @Autowired
    private CustomeCardService customeCardService;

    @RequestMapping("/save")
    public Result save(CustomeCard customeCard) throws ParseException {
        customeCard.setCreateDate(new Date());
        //2017-9-19 16:28:25
        customeCard.setBeginDate(StringUtils.formatDate2(customeCard.getBeginDateStr()));
        customeCard.setExpireDate(StringUtils.formatDate2(customeCard.getExpireDateStr()));
        customeCardService.save(customeCard);
        return new Result(false, Global.do_success,null,null);
    }


    //上传图片  /customecard/uploadPics?multipartFiles=图片文件
    @RequestMapping("/uploadPics")
    public UploadSuccessResult uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                //stringBuffer.append(",");
            }
        }
        return new UploadSuccessResult(stringBuffer.toString());
    }


}
