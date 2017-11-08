package com.haoyue.tuangou.utils;

/**
 * Created by LiJia on 2017/11/6.
 */
import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GenericRequest;
import com.haoyue.Exception.MyException;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云 OSS文件类
 *
 * @author YuanDuDu
 */
public class TOSSClientUtil {


    // endpoint以杭州为例，其它region请按实际情况填写
    private String endpoint = "oss-cn-beijing.aliyuncs.com";
    // accessKey
    private String accessKeyId = TGlobal.accessKeyId;
    private String accessKeySecret = TGlobal.accessKeySecret;
    //空间
    private String bucketName = "haoyue";
    //文件存储目录
    private String filedir = "hymarket/";
    private OSSClient ossClient;

    public TOSSClientUtil() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }


    public String uploadImg2Oss(MultipartFile file) throws MyException {

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        String name=year+"/"+(month+1)+"/"+ day+"/"+cal.getTimeInMillis()+substring;
        try {
            InputStream inputStream=null;
            if(!file.getOriginalFilename().contains("mp4")&&!file.getOriginalFilename().contains("rmvb")&&!file.getOriginalFilename().contains("avi")){
                inputStream = file.getInputStream();
            }
            this.uploadFile2OSS(inputStream, name,file);
            return name;
        } catch (Exception e) {
            throw new MyException("文件上传失败");
        }
    }

    /**
     * 获得图片路径
     * 视频和图片文件都可以上传到oss存储，通过外链可以在电脑浏览器中查看图片，却查看不了视频。
     例如  视频文件  http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/9/12/1505203245666.mp4
     图片文件  http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/9/12/1505202997797.jpg
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName,MultipartFile file) {
        //创建上传Object的Metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        String ret = "";
        if (instream==null&&file!=null){
            try {
                instream=file.getInputStream();
                String strContentType = file.getContentType();
                objectMetadata.setContentType(strContentType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            if(com.haoyue.untils.StringUtils.isNullOrBlank(objectMetadata.getContentType())) {
                objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            }
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            if (fileName.endsWith(".xlsx")){
                objectMetadata.setContentDisposition("attachment;filename=" + fileName);
            }
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
           System.out.println(e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase("xlsx")) {
            return "application/x-xls";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 删除文件
     */
    public  void delete(String key){

        try {
            GenericRequest request = new DeleteObjectsRequest(bucketName).withKey(key);
            ossClient.deleteObject(request);
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        // ossClient.deleteObject(bucketName, key);
    }
}

