package me.zhengjie;

import com.baidu.aip.ocr.AipOcr;
import me.zhengjie.modules.system.rest.WechatController;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.Ocr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EladminSystemApplicationTests {
    @Autowired
    private UserService userService;
    private static String token="25_pSoW44LgnRP3yX1ah0vQ9fvYyElwjv9sLepCd0Fzz2MtDR5BeyssbVLV-YmfRiKLLYdaKt5TpwRIOSVEIYBVrt8o1MEYIn2rP9QwSzpZcGkRPHbaCi5uF6o02QY6KwF5foajj9LHtpKFFDf8VFOjAJAAKT";
    @Test
    public void contextLoads() throws AWTException, InterruptedException, IOException, JSONException {
            //System.setProperty("java.awt.headless", "true");
        /*System.setProperty("java.awt.headless", "false");
        Robot robot =new Robot();
        robot.delay(5000);
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
        Thread.sleep(3000);
        robot.keyPress(KeyEvent.VK_F10);
        robot.keyRelease(KeyEvent.VK_F10);*/
        URL urlConet = new URL("http://mmbiz.qpic.cn/mmbiz_jpg/6iaTcOUEZ1YD9mTox4RIeUxibQcpqqKbLBH4dCzI8kDN1bNAOXYuEiamZ8Kyb9WZiaYYPd5xThCEPKurLygz60Vibpg/0");
        HttpURLConnection con = (HttpURLConnection)urlConet.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(4 * 1000);
        InputStream inStream = con .getInputStream();    //通过输入流获取图片数据
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        byte[] data =  outStream.toByteArray();
        AipOcr client = Ocr.getInstance();
        org.json.JSONObject res = client.basicGeneral(data, new HashMap<String, String>());
        JSONArray wordsResult = res.getJSONArray("words_result");
        int index = 0;
        for (int i = 0; i < wordsResult.length(); i++) {
            org.json.JSONObject jsonObject = wordsResult.getJSONObject(i);
            String words = jsonObject.get("words").toString();

            System.out.println(wordsResult);
            if (words.equals("转账备注") || words.equals("转账")||words.equals("转专账备注")) {
                index = i;
                break;
            }
            //JSONObject jsonObject = wordsResult.getJSONObject(15);
        }
        JSONObject jsonObject = wordsResult.getJSONObject(index + 1);
        String words = jsonObject.get("words").toString();
        System.out.println(words.substring(0,13));
    }


    @Test
    public void contextLoads1() throws Exception {
        sendModelMessage("oXhzV1KgW4QCwULOkhm4sD2mMmO8","发送者：发帖人\n订单号：ZW190909005","发帖反馈\n反馈内容：亲，这个价格已经是成本价了，不能少了，你直接发吧！","亲，请前往asinone在线下单系统进行沟通","");
    }

    public static Object sendModelMessage(String openid,String firstValue,String keyword1Value,String keyword2Value,String keyword3Value,String keyword4Value,String remarkValue,String url,String color) throws Exception {
        String accessToken = "25_pSoW44LgnRP3yX1ah0vQ9fvYyElwjv9sLepCd0Fzz2MtDR5BeyssbVLV-YmfRiKLLYdaKt5TpwRIOSVEIYBVrt8o1MEYIn2rP9QwSzpZcGkRPHbaCi5uF6o02QY6KwF5foajj9LHtpKFFDf8VFOjAJAAKT";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Token token = asinInfoDao.getToken();
        //String accessToken = token.getToken();
        com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject text=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject keyword1=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject keyword2=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject keyword3=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject keyword4=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject first=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject remark=new com.alibaba.fastjson.JSONObject();
        json.put("touser",openid);
        json.put("template_id","vz6_dBewmFuSp_nbBUR0Vxk58v14uvOx0PNnVwuvWmY");
        //String replace = followDetails.getShopName().replace(" ", "");
        //String replace1 = replace.replace("&", "");
        json.put("url", url);
        first.put("value",firstValue);
        keyword1.put("value",keyword1Value);
        keyword2.put("value",keyword2Value );
        keyword3.put("value",keyword3Value);
        keyword4.put("value",keyword4Value );
        remark.put("value", remarkValue);
        remark.put("color", color);
        text.put("Apply_id", keyword1);
        text.put("Apply_Type", keyword2);
        text.put("Apply_State", keyword3);
        text.put("Apply_CreateTime", keyword4);
        text.put("first", first);
        text.put("remark",remark);
        json.put("data", text);
        //log.info("开始发送信息");
        Map<String, Object> map1 = WechatController.httpClientPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, json.toJSONString());
        System.out.println( map1.get("errcode"));
        return map1.get("errcode");
    }

    public static Object sendModelMessage(String openid,String firstValue,String keyword1Value,String remarkValue,String url) throws Exception {
        String accessToken = token;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Token token = asinInfoDao.getToken();
        //String accessToken = token.getToken();
        com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject text=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject keyword1=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject keyword2=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject first=new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject remark=new com.alibaba.fastjson.JSONObject();
        json.put("touser",openid);
        json.put("template_id","_icVHKlcNKsz_8HDENcUtLpiajo7I-mJ5MiOwQE9-Cg");
        //String replace = followDetails.getShopName().replace(" ", "");
        //String replace1 = replace.replace("&", "");
        json.put("url", url);
        first.put("value",firstValue);
        keyword1.put("value",keyword1Value);
        keyword2.put("value",simpleDateFormat.format(date) );
        remark.put("value", remarkValue);
        text.put("keyword1", keyword1);
        text.put("keyword2", keyword2);
        text.put("first", first);
        text.put("remark",remark);
        json.put("data", text);
        //log.info("开始发送信息");
        Map<String, Object> map1 = WechatController.httpClientPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, json.toJSONString());
        System.out.println( map1.get("errcode"));
        return map1.get("errcode");
    }
    @Test
    public void strings(){
        String sss="2019-09-18 23:01:00";
        String substring = sss.substring(8,10);
        System.out.println(substring);
        int i = Integer.parseInt(substring);
        System.out.println(i+1);
    }
}

