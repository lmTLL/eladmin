package me.zhengjie.modules.system.rest;

import me.zhengjie.modules.system.domain.FileStatus;
import me.zhengjie.modules.system.repository.FileStatusRepository;
import me.zhengjie.modules.system.service.FileStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;

/**
 * @author groot
 * @date 2019-07-09
 */
@RestController
public class Test {
    @Autowired
    private FileStatusRepository fileStatusRepository;
    @Autowired
    private FileStatusService fileStatusService;
    @Autowired
    private WechatController wechatController;
    private String TOKEN = "pangxianwei";
    @GetMapping("/weChat/callback")
    public String test(@RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("echostr")String echostr) {

        //排序
        String sortString = sort(TOKEN, timestamp, nonce);
        //加密
        String myString = sha1(sortString);
        //校验
        if (myString != null && myString !="" && myString.equals(signature)) {
            System.out.println("签名校验通过");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            System.out.println("签名校验失败");
            return "";
        }
    }

    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    @GetMapping("/MP_verify_27cPHIW5VRtHcomQ.txt")
    public String get(){
        return "27cPHIW5VRtHcomQ";
    }

    @GetMapping("/test/sendMessage")
    @ResponseBody
    public String sendMessage(String openId,String message) throws Exception {
        String filename = message.substring(message.indexOf("【")+1, message.lastIndexOf("】"));
        Map<String, Object> userInfoByOpenid = wechatController.getUserInfoByOpenid(openId);
        Object nickname = userInfoByOpenid.get("nickname");
        String nicknames="s";
        if (nickname!=null){
            nicknames=nickname.toString();
        }
        fileStatusRepository.sign(filename,nicknames);
        System.out.println(filename);
        Object msg = sendModelMessage(openId, message, "", "", "", "", "", "","");
            return msg.toString();
    }

    @GetMapping("/test/signText")
    //@ResponseBody
    public void signText(String nickname, String filename, HttpServletResponse response) throws IOException {
        FileStatus fileStatusByNickname = fileStatusRepository.findFileStatusByNickname(nickname, filename);
        if (fileStatusByNickname==null){
            FileStatus fileStatus=new FileStatus();
            fileStatus.setFilename(filename);
            fileStatus.setNickname(nickname);
            fileStatus.setNewStatus("1");
            fileStatusService.create(fileStatus);
            response.setHeader("Access-Control-Allow-Origin","*");
            response.getWriter().print("1");
        }else {
            response.setHeader("Access-Control-Allow-Origin","*");
            response.getWriter().print(fileStatusByNickname.getNewStatus());
           // return fileStatusByNickname.getNewStatus();
        }
    }

    @GetMapping("/test/doSign")
    public void doSign(String nickname,String filename){
        fileStatusRepository.signByNickname(filename, nickname);
    }
}