package com.haoyue.untils;

/**
 * Created by LiJia on 2017/9/11.
 * 卖家后台文件上传插件 上传失败  必须有 error 字段
 */
public class UploadFailResult {

    private String error;


    public UploadFailResult(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
