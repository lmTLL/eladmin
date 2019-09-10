package me.zhengjie.modules.system.rest;

import com.google.gson.Gson;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.AsinInfoService;
import me.zhengjie.modules.system.service.dto.AsinInfoQueryCriteria;
import me.zhengjie.modules.system.service.impl.AmzWebCrawlerServiceImpl;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
/**
* @author groot
* @date 2019-07-22
*/
@RestController
@RequestMapping("api")
public class AsinInfoController {
    @Autowired
    private AmzWebCrawlerServiceImpl amzWebCrawlerService;
    @Autowired
    private AsinInfoService asinInfoService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WechatController wechatController;
    @Log("查询AsinInfo")
    @GetMapping(value = "/asinInfo")
    @PreAuthorize("hasAnyRole('ADMIN','ASININFO_ALL','ASININFO_SELECT')")
    public ResponseEntity getAsinInfos(AsinInfoQueryCriteria criteria, Pageable pageable){
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId()==2){
                criteria.setOpenId(byUsername.getOpenId());
            }
        }
        Object o = asinInfoService.queryAll(criteria, pageable);
        System.out.println(o.toString());
        return new ResponseEntity(asinInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增AsinInfo")
    @PostMapping(value = "/asinInfo")
    @PreAuthorize("hasAnyRole('ADMIN','ASININFO_ALL','ASININFO_CREATE')")
    public ResponseEntity create(@Validated @RequestBody AsinInfo resources) throws IOException, InterruptedException {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        resources.setOpenId(byUsername.getOpenId());
        resources.setCount(resources.getStartCount());
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        resources.setStartDate(timestamp);
        resources.setUpdateDate(timestamp);
        try {
            AsinInfo aSin = nicknaeHead(resources.getOpenId());
            resources.setNickName(aSin.getNickName());
            resources.setHeadImgurl(aSin.getHeadImgurl());
        }catch (Exception e){
            System.out.println("找不到昵称，头像URL");
            System.out.println(e);
        }
        ResponseEntity returning = new ResponseEntity(asinInfoService.create(resources),HttpStatus.CREATED);
        runReptile(resources);
        return returning;
    }

    @Log("修改AsinInfo")
    @PutMapping(value = "/asinInfo")
    @PreAuthorize("hasAnyRole('ADMIN','ASININFO_ALL','ASININFO_EDIT')")
    public ResponseEntity update(@Validated @RequestBody AsinInfo resources) throws IOException, InterruptedException {
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        resources.setUpdateDate(timestamp);
        asinInfoService.update(resources);
        runReptile(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除AsinInfo")
    @DeleteMapping(value = "/asinInfo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ASININFO_ALL','ASININFO_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        asinInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    /**
     *  利用openId获取用户昵称和头像URI
     * @param openid
     * @return
     * @throws Exception
     */
    public AsinInfo nicknaeHead(String openid) throws Exception {

        String access_tocken = wechatController.getAccessToken();
        System.out.println("==================token:"+access_tocken);
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_tocken + "&openid=" + openid;
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        GetMethod httpGet = new GetMethod(url);
        try {
            client.executeMethod(httpGet);
            String response = httpGet.getResponseBodyAsString();
            Gson gson = new Gson();
            Map<String, Object> wechatUserInfoMap = gson.fromJson(response, Map.class);
            AsinInfo asinInfo =new AsinInfo();
            if (wechatUserInfoMap.get("nickname") != null) {
                asinInfo.setNickName(wechatUserInfoMap.get("nickname").toString());
            }
            if (wechatUserInfoMap.get("headimgurl") != null){
                asinInfo.setHeadImgurl(wechatUserInfoMap.get("headimgurl").toString());
            }
            return asinInfo;
        } catch (Exception e) {
            throw e;
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * 爬虫方法
     * @param resources 用户类
     * @throws InterruptedException
     * @throws IOException
     */
    public void runReptile(AsinInfo resources) throws InterruptedException, IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<AsinInfo> asinInfos = new ArrayList<>();
                    AsinInfo newResources =  asinInfoService.getByAsin(resources);
                    asinInfos.add(newResources);
                    amzWebCrawlerService.getAmzProductValueTask(asinInfos);
                    System.out.println("已经执行了！");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("失败1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("失败2");
                }
            }
        }).start();
    }

}