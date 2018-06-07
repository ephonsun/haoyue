package com.haoyue;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;


import com.haoyue.untils.*;
import com.haoyue.untils.HttpRequest;
import io.goeasy.GoEasy;


import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HaoyueApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(new Date().toLocaleString());
    }

    @Test
    public void jiemi() {
        WXAppletUserInfo wxAppletUserInfo = new WXAppletUserInfo();
        String encryptedData = "";
        java.lang.String iv = "YlU0Fg7wxTacPh1kgARdRw==";
        String session_key = "e6hDqemGTaSBDRm9NpF24A==";
        wxAppletUserInfo.decodeUserInfo(encryptedData, iv, session_key);
    }

    @Test
    public void f3() {
        String appId = "wx2be4cbdc761d212f";
        String code = "";
        String secret = "b252ea6c94816a64f1d177ab0734668c";
        String response = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", "appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code");
        System.out.println(response);
    }

}
