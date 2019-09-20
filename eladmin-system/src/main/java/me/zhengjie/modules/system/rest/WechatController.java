package me.zhengjie.modules.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.ocr.AipOcr;
import com.google.gson.Gson;
import io.micrometer.core.instrument.util.StringUtils;
import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.repository.ChannelRepository;
import me.zhengjie.modules.system.repository.KeyMsgRepository;
import me.zhengjie.modules.system.repository.SaleOrderRepository;
import me.zhengjie.modules.system.service.InvitationcodesService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.RoleSmallDTO;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.utils.Ocr;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/*import com.alittle.demo.dao.AsinInfoDao;
import com.alittle.demo.entity.TestMessage;*/
/**
 * @author groot
 * @date 2019-07-09
 */
@Controller
public class WechatController {

    //测试公众号
    private static final String app_id = "wx597e5f7c95d6fbc0";
    private static final String app_secret = "c7a20e14d1499637baf1e66b9cffb043";
    private static final Gson gson = new Gson();
    private static final Logger log = LoggerFactory.getLogger("adminLogger");
    public static String content_openid = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationcodesService invitationcodesService;
    @Autowired
    private SaleOrderRepository saleOrderRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private KeyMsgRepository keyMsgRepository;
    public static String token;
    /***
     * httpClient-Get请求
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static Map<String, Object> httpClientGet(String url) throws Exception {
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        GetMethod httpGet = new GetMethod(url);
        try {
            client.executeMethod(httpGet);
            String response = httpGet.getResponseBodyAsString();
            Map<String, Object> map = gson.fromJson(response, Map.class);
            return map;
        } catch (Exception e) {
            throw e;
        } finally {
            httpGet.releaseConnection();
        }
    }

    /***
     * httpClient-Post请求
     * @param url 请求地址
     * @param params post参数
     * @return
     * @throws Exception
     */
    public static Map<String, Object> httpClientPost(String url, String params) throws Exception {
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        PostMethod httpPost = new PostMethod(url);
        try {
            RequestEntity requestEntity = new ByteArrayRequestEntity(params.getBytes("utf-8"));
            httpPost.setRequestEntity(requestEntity);
            client.executeMethod(httpPost);
            String response = httpPost.getResponseBodyAsString();
            Map<String, Object> map = gson.fromJson(response, Map.class);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            httpPost.releaseConnection();
        }
    }
    @GetMapping("/test/sss")
    @ResponseBody
    public static String saveAccess_token() throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + app_id + "&secret=" + app_secret;
        Map<String, Object> accessTokenMap = WechatController.httpClientGet(url);
        System.out.println(accessTokenMap);
        token=accessTokenMap.get("access_token").toString();
        System.out.println(token);
        //redisTemplate.opsForValue().set("access_token",access_token);
        return token;
    }
    @GetMapping("/test/token")
    @ResponseBody
    public String getAccessToken() throws Exception {
        return token;
    }

    @GetMapping("/test/callJason")
    @ResponseBody
    public String callJason() throws Exception {
        sendModelMessage("oXhzV1B52PHJKXQ_c9zN_EbM8wkU","有新的订单需要支付！","","","","","","","");
        //sendModelMessage("oXhzV1JX_1fr9w30j5T6m99T6hWc","有新的订单需要支付！","","","","","","","");
        return "ok";
    }
    // 通过openid获取用户信息
    public Map<String, Object> getUserInfoByOpenid(String openid) throws Exception {
        String access_tocken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_tocken + "&openid=" + openid;
        Map<String, Object> map = httpClientGet(url);
        return map;
    }


    @GetMapping("/mpLogin")
    @ResponseBody
    public Map<String, Object> wechatMpLogin() throws Exception {
        //Jedis jedis = new Jedis("localhost");
        ModelMap modelMap = new ModelMap();
        String access_token = getAccessToken();
        System.out.println(access_token);
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
        String scene_str = "yidiandian" + new Date().getTime();
        String params = "{\"expire_seconds\":600, \"action_name\":\"QR_STR_SCENE\", \"action_info\":{\"scene\":{\"scene_str\":\"" + scene_str + "\"}}}";
        Map<String, Object> resultMap = httpClientPost(url, params);
        System.out.println("params" + params);
        System.out.println("code" + resultMap);
        if (resultMap.get("ticket") != null) {
            String qrcodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + resultMap.get("ticket");
            modelMap.put("qrcodeUrl", qrcodeUrl);
        }
        //asinInfoDao.updateToken(jedis.get("access_token"));
        modelMap.put("scene_str", scene_str);
        return modelMap;
    }
    @RequestMapping("/checkLogin/{scene_str}")
    public @ResponseBody
    Object wechatMpCheckLogin(@PathVariable String scene_str) throws Exception {
        Object openId=null;
        if (scene_str!=null&&!"".equals(scene_str)){
            openId= redisTemplate.opsForValue().get(scene_str);
        }
        System.out.println("scene_str:"+scene_str);
        System.out.println("openid:"+openId);
        if (openId!=null){
            //System.out.println(openId.toString());
            Map<String, Object> wechatUserInfoMap = getUserInfoByOpenid(openId.toString());
            System.out.println(wechatUserInfoMap);
            UserDTO byOpenId = userService.findByOpenId(openId.toString());
            if (byOpenId!=null){
                byOpenId.setOpenId(openId.toString());
                return byOpenId;
            }else {
                Map<String,Object> map=new HashMap<>();
                map.put("nickname",wechatUserInfoMap.get("nickname"));
                map.put("headimgurl",wechatUserInfoMap.get("headimgurl"));
                map.put("openid",openId);
                return map;
            }
        }
        return null;
    }
    /*@RequestMapping(value = "/weChat/callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        Map<String, String> callbackMap = xmlToMap(httpServletRequest); //获取回调信息
        if (callbackMap != null && callbackMap.get("FromUserName").toString() != null) {
            Map<String, Object> wechatUserInfoMap = getUserInfoByOpenid(callbackMap.get("FromUserName"));
            String accessToken = getAccessToken();
            if (callbackMap.get("EventKey") != null) {
                System.out.println(callbackMap.get("FromUserName"));
                String replaceEventKey = callbackMap.get("EventKey").replace("qrscene_", "");
                redisTemplate.opsForValue().set(replaceEventKey,callbackMap.get("FromUserName"));
                sendMessage(callbackMap.get("FromUserName"),"亲，终于等到你，我们可以：注册VS转让商标/专利/打跟卖/A+/上视频/卖家账号/站外推广/真人测评V绿标/修复链接/申诉/信用卡/类目审核等等。快快加右下角客服微信了解吧。");
            }
        }
    }*/


    @RequestMapping(value = "/weChat/callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        Map<String, String> callbackMap = xmlToMap(httpServletRequest); //获取回调信息
        System.out.println(callbackMap.get("MsgType"));
        System.out.println(callbackMap.get("Event"));
        String openId = callbackMap.get("FromUserName");
        String toUserName = callbackMap.get("ToUserName");
        String replaceEventKey="";
        if (callbackMap.get("EventKey")!=null){
            replaceEventKey= callbackMap.get("EventKey").replace("qrscene_", "");
        }
        if (replaceEventKey.length()>10){
            if (callbackMap.get("EventKey") != null&&"yidiandian".equals(replaceEventKey.substring(0,10))) {
                redisTemplate.opsForValue().set(replaceEventKey,openId);
                sendMessage(openId,keyMsgRepository.findMsgByKey("hello"));
            }
        }
        if("text".equals(callbackMap.get("MsgType"))){
            List<Channel> channelByOpenId = channelRepository.findChannelByOpenId(openId);
            if (channelByOpenId.size()>0){
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMM");
                saleOrderRepository.signHandleBySaleNumber("DGM"+simpleDateFormat.format(date)+callbackMap.get("Content"),channelByOpenId.get(0).getUserId());
                sendMessage(openId,"温馨提示:订单号-DGM"+simpleDateFormat.format(date)+callbackMap.get("Content")+"在系统内已标记处理，无需打开网页标记。");
                sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk",callbackMap.get("Content"),"","赶跟卖","","","渠道："+channelByOpenId.get(0).getChannelName(),"","");
                sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE",callbackMap.get("Content"),"","赶跟卖","","","渠道："+channelByOpenId.get(0).getChannelName(),"","");
            }
            UserDTO byOpenId = userService.findByOpenId(openId);
            if (byOpenId!=null){
                Set<RoleSmallDTO> roles = byOpenId.getRoles();
                for (RoleSmallDTO role : roles) {
                    if (role.getId()==5||role.getId()==7){
                        String string = "";
                        Date date=new Date();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddssmm");
                        // 循环得到10个字母
                        for (int i = 0; i < 5; i++) {

                            // 得到随机字母
                            char c = (char) ((Math.random() * 26) + 97);
                            // 拼接成字符串
                            string += (c + "");
                        }
                        Invitationcodes invitationcodes=new Invitationcodes();
                        invitationcodes.setUsername(byOpenId.getUsername());
                        invitationcodes.setInvitationCode(string+sdf.format(date));
                        invitationcodes.setVxId(callbackMap.get("Content"));
                        invitationcodes.setEnable("0");
                        invitationcodesService.create(invitationcodes);
                        sendMessage(openId,"邀请码："+invitationcodes.getInvitationCode());
                    }
                }
            }
            if ("oXhzV1NEM5Leb1II8PbXxBcgIFjk".equals(openId)||"oXhzV1B52PHJKXQ_c9zN_EbM8wkU".equals(openId)||"oXhzV1KgW4QCwULOkhm4sD2mMmO8".equals(openId)||"oXhzV1KB4kPAdpJiarAFgiJun9FE".equals(openId)){
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    httpClientGet("http://39.98.168.25:8082/member/testSend/"+openId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();
            }
        }else if ("image".equals(callbackMap.get("MsgType"))){
            if ("oXhzV1KgW4QCwULOkhm4sD2mMmO8".equals(openId)||"oXhzV1B52PHJKXQ_c9zN_EbM8wkU".equals(openId)||"oXhzV1KB4kPAdpJiarAFgiJun9FE".equals(openId)||"oXhzV1JX_1fr9w30j5T6m99T6hWc".equals(openId)){
                System.out.println(callbackMap.get("PicUrl"));
                URL urlConet = new URL(callbackMap.get("PicUrl"));
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
                    if (words.equals("订单") || words.equals("订单号")) {
                        index = i;
                        break;
                    }
                    //JSONObject jsonObject = wordsResult.getJSONObject(15);
                }
                org.json.JSONObject jsonObject = wordsResult.getJSONObject(index + 1);
                String words = jsonObject.get("words").toString();

                int index1 = 0;
                for (int i = 0; i < wordsResult.length(); i++) {
                    org.json.JSONObject jsonObject1 = wordsResult.getJSONObject(i);
                    String words1 = jsonObject1.get("words").toString();
                    if (words1.equals("转账备注") || words1.equals("转账")||words1.equals("转专账备注")) {
                        index1 = i;
                        break;
                    }
                    //JSONObject jsonObject = wordsResult.getJSONObject(15);
                }
                org.json.JSONObject jsonObject1 = wordsResult.getJSONObject(index1 + 1);
                String words1 = jsonObject1.get("words").toString();
                System.out.println("订单号"+words);
                System.out.println("转账备注"+words1.substring(0,13));
                Map<String, Object> map = httpClientGet("http://39.98.168.25:8082/purchaseOrders/uploadPic?purchaseOrderNo=" + words1.substring(0, 13) + "&paymentId=" + URLEncoder.encode(words,"utf-8") + "&paymentScreenshot=" + callbackMap.get("PicUrl"));
                Object msg = map.get("msg");
                if ("0".equals(msg)){
                    sendMessage(openId,"上传失败,识别单号："+words+",识别备注："+words1.substring(0, 13));
                }else if ("1".equals(msg)){
                    sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE","付款截图已上传",words1.substring(0, 13),"","","","","","");
                    sendMessage(openId,"上传成功,识别单号："+words+",识别备注："+words1.substring(0, 13));
                }
            }
        }else if ("event".equals(callbackMap.get("MsgType"))){
            //扫码事件 关注登录
            if ("subscribe".equals(callbackMap.get("Event"))){
                //String replaceEventKey = callbackMap.get("EventKey").replace("qrscene_", "");
                //redisTemplate.opsForValue().set(replaceEventKey,openId);
                //sendMessage(openId,"亲，终于等到你，我们可以：注册VS转让商标/专利/打跟卖/A+/上视频/卖家账号/站外推广/真人测评V绿标/修复链接/申诉/信用卡/类目审核等等。快快加右下角客服微信了解吧。");
            }else if("CLICK".equals(callbackMap.get("Event"))){
                String key=callbackMap.get("EventKey");
                if ("newEvent".equals(key)){
                    //initImageMessage(keyMsgRepository.findMsgByKey(key),openId,toUserName);
                    String msgByKey = keyMsgRepository.findMsgByKey(key);
                    String replace = msgByKey.replace("<br>", "\n");
                    sendMessage(openId,replace);
                }else if ("addSaleOrder".equals(key)){
                    String msgByKey = keyMsgRepository.findMsgByKey(key);
                    String replace = msgByKey.replace("<br>", "\n");
                    sendMessage(openId,replace);
                }else if ("codeImg".equals(key)){
                    initImageMessage(keyMsgRepository.findMsgByKey(key),openId,toUserName);
                    //sendMessage(openId,keyMsgRepository.findMsgByKey(key));
                }
            }
        }
    }



    // xml转为map
    private Map<String, String> xmlToMap(HttpServletRequest httpServletRequest) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            InputStream inputStream = httpServletRequest.getInputStream();
            SAXReader reader = new SAXReader(); // 读取输入流
            org.dom4j.Document document = reader.read(inputStream);
            Element root = document.getRootElement(); // 得到xml根元素
            List<Element> elementList = root.elements(); // 得到根元素的所有子节点
            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());
            // 释放资源
            inputStream.close();
            inputStream = null;
            return map;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }
    /**
     * 微信发送消息
     * @param openid 接收者openid
     * @param message 发送信息
     * @throws Exception
     */
    public static void sendMessage(String openid,String message) throws Exception {
        String accessToken = token;
        String messageUrl="";
        if (accessToken!=null){
            messageUrl = content_openid.replace("ACCESS_TOKEN", accessToken);
        }
        TestMessage testMessage = new TestMessage();
        testMessage.setMsgtype("text");
        testMessage.setTouser(openid);
        Map<String, Object> map = new HashMap<>();
        map.put("content",message);
        testMessage.setText(map);
        String jsonTestMessage = JSONObject.toJSONString(testMessage);
        if (accessToken!=null){
            WechatController.httpClientPost(messageUrl, jsonTestMessage);
        }
    }


    /**
     * 微信发送模板消息-服务提交成功
     * @throws Exception
     */
    public static Object sendModelMessage(String openid,String firstValue,String keyword1Value,String keyword2Value,String keyword3Value,String keyword4Value,String remarkValue,String url,String color) throws Exception {
        String accessToken = token;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Token token = asinInfoDao.getToken();
        //String accessToken = token.getToken();
        JSONObject json=new JSONObject();
        JSONObject text=new JSONObject();
        JSONObject keyword1=new JSONObject();
        JSONObject keyword2=new JSONObject();
        JSONObject keyword3=new JSONObject();
        JSONObject keyword4=new JSONObject();
        JSONObject first=new JSONObject();
        JSONObject remark=new JSONObject();
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

    /**
     * 微信发送模板消息-意见反馈通知
     * @throws Exception
     */
    public static Object sendModelMessage(String openid,String firstValue,String keyword1Value,String remarkValue,String url,String color) throws Exception {
        String accessToken = token;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Token token = asinInfoDao.getToken();
        //String accessToken = token.getToken();
        JSONObject json=new JSONObject();
        JSONObject text=new JSONObject();
        JSONObject keyword1=new JSONObject();
        JSONObject keyword2=new JSONObject();
        JSONObject first=new JSONObject();
        JSONObject remark=new JSONObject();
        json.put("touser",openid);
        json.put("template_id","_icVHKlcNKsz_8HDENcUtLpiajo7I-mJ5MiOwQE9-Cg");
        //String replace = followDetails.getShopName().replace(" ", "");
        //String replace1 = replace.replace("&", "");
        json.put("url", url);
        first.put("value",firstValue);
        keyword1.put("value",keyword1Value);
        keyword2.put("value",simpleDateFormat.format(date) );
        remark.put("value", remarkValue);
        remark.put("color", color);
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


    public static void initImageMessage(String MediaId,String toUserName,String fromUserName){
        String accessToken = token;
        String messageUrl="";
        if (accessToken!=null){
            messageUrl = content_openid.replace("ACCESS_TOKEN", accessToken);
        }
        Image image = new Image();
        ImageMessage imageMessage = new ImageMessage();
        image.setMediaid(MediaId);
        imageMessage.setFromuser(fromUserName);
        imageMessage.setTouser(toUserName);
        Date date=new Date();
        imageMessage.setCreatetime(date.getTime()+"");
        imageMessage.setImage(image);
        imageMessage.setMsgtype("image");
        String jsonTestMessage = JSONObject.toJSONString(imageMessage);
        System.out.println(jsonTestMessage);
        if (accessToken!=null){
            try {
                Map<String, Object> map = WechatController.httpClientPost(messageUrl, jsonTestMessage);
                System.out.println(map.get("errcode"));
                System.out.println(map.get("errmsg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
