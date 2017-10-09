package com.haoyue;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.haoyue.untils.Global;
import com.haoyue.untils.HttpRequest;
import com.haoyue.untils.Result;
import com.haoyue.untils.WXAppletUserInfo;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HaoyueApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println(new Date().toLocaleString());
	}

	@Test
	public void jiemi(){
		WXAppletUserInfo wxAppletUserInfo=new WXAppletUserInfo();
		String encryptedData="";
		String iv="YlU0Fg7wxTacPh1kgARdRw==";
		String session_key="e6hDqemGTaSBDRm9NpF24A==";
		wxAppletUserInfo.decodeUserInfo(encryptedData,iv,session_key);
	}

	@Test
	public void  f3(){
		String appId="wx2be4cbdc761d212f";
		String code="";
		String secret="b252ea6c94816a64f1d177ab0734668c";
		String response= HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session","appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code");
		System.out.println(response);
	}
	//customer/getPhone?encryptedData=12&iv=12&

	@Test
	public void f4(){
		System.out.println(Integer.parseInt("我"));
	}

	//获取支付信息
	//@EngineFunction("getPayInformation")
//	public static Map<String, Object> getPayInformation(String orderId) throws AVException, UnsupportedEncodingException, DocumentException {
//		Map<String, Object> reqMap = new TreeMap<String, Object>(
//				new Comparator<String>() {
//					public int compare(String obj1, String obj2) {
//						// 升序排序
//						return obj1.compareTo(obj2);
//					}
//				});
//		if (AVUser.getCurrentUser() != null) {
//			String authDataJson = JSONArray.toJSONString(AVUser.getCurrentUser().get("authData"));
//			JSONObject jsonObject = JSON.parseObject(authDataJson);
//			jsonObject.get("lc_weapp");
//			JSONObject j2 = JSON.parseObject(jsonObject.get("lc_weapp").toString());
//			String openId = (String) j2.get("openid");
//			AVQuery<Order> query = AVObject.getQuery(Order.class);
//			Order order = query.get(orderId);
//			reqMap.put("appid", System.getenv("appid"));
//			reqMap.put("mch_id", System.getenv("mch_id"));
//			reqMap.put("nonce_str", WXPayUtil.getNonce_str());
//			reqMap.put("body", new String(order.getDishesList().toString().getBytes("UTF-8")));
//			reqMap.put("openid", openId);
//			reqMap.put("out_trade_no", order.getObjectId());
//			reqMap.put("total_fee", 1); //订单总金额，单位为分
//			reqMap.put("spbill_create_ip", "192.168.0.1"); //用户端ip
//			reqMap.put("notify_url", System.getenv("notify_url")); //通知地址
//			reqMap.put("trade_type", System.getenv("trade_type")); //trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识
//			String reqStr = WXPayUtil.map2Xml(reqMap);
//			String resultXml = HttpRequest.sendPost(reqStr);
//			System.out.println("微信请求返回:" + resultXml);
//			//解析微信返回串 如果状态成功 则返回给前端
//			if (WXPayUtil.getReturnCode(resultXml) != null && WXPayUtil.getReturnCode(resultXml).equals("SUCCESS")) {
//				//成功
//				Map<String, Object> resultMap = new TreeMap<>(
//						new Comparator<String>() {
//							public int compare(String obj1, String obj2) {
//								// 升序排序
//								return obj1.compareTo(obj2);
//							}
//						});
//				resultMap.put("appId", System.getenv("appid"));
//				resultMap.put("nonceStr", WXPayUtil.getNonceStr(resultXml));//解析随机字符串
//				resultMap.put("package", "prepay_id=" + WXPayUtil.getPrepayId(resultXml));
//				resultMap.put("signType", "MD5");
//				resultMap.put("timeStamp", String.valueOf((System.currentTimeMillis() / 1000)));//时间戳
//				String paySign = WXPayUtil.getSign(resultMap);
//				resultMap.put("paySign", paySign);
//				return resultMap;
//			} else {
//				throw new AVException(999, "微信请求支付失败");
//			}
//		} else {
//			throw new AVException(98, "当前未登录用户");
//		}
//	}


	public void upload(){
		String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
		String accessKeyId =  Global.accessKeyId;
		String accessKeySecret = Global.accessKeySecret;
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);
		// 上传
		byte[] content = "Hello OSS".getBytes();
		PutObjectResult putObjectResult=ossClient.putObject("haoyue", "<yourKey>", new ByteArrayInputStream(content));
		System.out.println(putObjectResult);
		// 关闭client
		ossClient.shutdown();
	}

	@Test
	public void f5(){
		Date from=new Date();
		Date to=new Date();
		from.setDate(1);
		from.setHours(0);
		from.setMinutes(0);
		from.setSeconds(0);
		System.out.println(from.toLocaleString());
		System.out.println(to.toLocaleString());
	}

	@Test
	public void f6(){
		int m=-1;
		int n=10;
		System.out.println(m+n);
	}

	@Test
	public void f7() throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		Date nomat= sdf.parse("20170919162825");
//		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date format=sdf2.parse(nomat.toString());
		System.out.print(nomat.toLocaleString());
	}

	@Test
	public void f8(){
		Boolean flag=null;
		System.out.println("flag="+flag);
		flag=true;
		System.out.println("flag="+flag);
	}

	@Test
	public void f9(){
		Date date=new Date();
		System.out.println("当前日期=="+date.toLocaleString());
		date.setYear(date.getYear()+20);
		System.out.println("一年后日期=="+date.toLocaleString());
	}

	@Test
	public void f10(){
		String relativelyPath=System.getProperty("user.dir");
		System.out.println(relativelyPath);
	}

	@Test
	public void f11(){
		List<String> list=new ArrayList<>();
		list.add("a");
		list.add("b");
		System.out.println(list.size());
		list.clear();
		System.out.println(list.size());
		list.add("a");
		list.add("b");
		System.out.println(list.size());
	}

	@Test
	public void f12(){
	 String str="http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/9/14/1505380758254.avi";
		System.out.println(str.substring(str.indexOf("hymarket")));
	}


}
