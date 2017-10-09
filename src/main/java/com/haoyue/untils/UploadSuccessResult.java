package com.haoyue.untils;

/**
 * Created by LiJia on 2017/8/31.
 * 文件上传插件 上传成功 不能有 error字段
 */
public class UploadSuccessResult {

    private Object data;

    public UploadSuccessResult(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
