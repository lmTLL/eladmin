package me.zhengjie.modules.system.rest;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.repository.ChannelRepository;
import me.zhengjie.modules.system.repository.SaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.repository.WaitPaymentRepository;
import me.zhengjie.modules.system.service.AmzConfigService;
import me.zhengjie.modules.system.service.ChannelService;
import me.zhengjie.modules.system.service.SaleOrderService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.*;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static me.zhengjie.modules.system.rest.WechatController.httpClientGet;
import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;
import static me.zhengjie.utils.ExcelUtil.excelExport;


/**
 * @author groot
 * @date 2019-07-09
 */
@RestController
@RequestMapping("api")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private WechatController wechatController;
    @Autowired
    private SaleOrderRepository saleOrderRepository;
    @Autowired
    private WaitPaymentRepository waitPaymentRepository;
    @Autowired
    private AmzConfigService amzConfigService;
    public static String content_openid = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    @Log("查询SaleOrder")
    @GetMapping(value = "/saleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_SELECT')")
    public ResponseEntity getSaleOrders(SaleOrderQueryCriteria criteria, Pageable pageable) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        criteria.setCustomerId(byUsername.getId());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId() == 1 || role.getId() == 7 || role.getId() == 4) {
                criteria.setCustomerId(null);
            }
            if (role.getId() == 6) {
                String openId = byUsername.getOpenId();
                Long id = byUsername.getId();
                criteria.setCustomerId(null);
                criteria.setStatus("2");
                criteria.setChannelUserId(id);
                //List<Channel> channelByOpenId = channelRepository.findChannelByOpenId(openId);
                //criteria.setChannelName(channelByOpenId.get(0).getChannelName());
            }
            if (role.getId() == 5) {
                criteria.setCustomerId(null);
                String invitation = byUsername.getUsername();
                criteria.setInvitation(invitation);
            }
        }
        return new ResponseEntity(saleOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增SaleOrder")
    @PostMapping(value = "/saleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SaleOrder resources) throws Exception {
        resources.setStatus("1");
        resources.setNewOrder("0");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        if (resources.getAccountOrder() != null && !"".equals(resources.getAccountOrder())) {
            //resources.setRemark("");
            resources.setStatus("2");
            resources.setAccountTime(timestamp);
        }
        resources.setStartTime(timestamp);
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        String customerOpenId = byUsername.getOpenId();
        String invitation = byUsername.getInvitation();
        resources.setCustomerId(byUsername.getId());
        resources.setInvitation(invitation);
        ChannelDTO channel = channelService.findById(resources.getChannelId());
        resources.setChannelName(channel.getChannelName());
        System.out.println(channel.getUserId());
        resources.setChannelUserId(channel.getUserId());
        String accessToken = WechatController.token;
        String messageUrl = "";
        if (accessToken != null) {
            messageUrl = content_openid.replace("ACCESS_TOKEN", accessToken);
        }
        TestMessage testMessage = new TestMessage();
        testMessage.setMsgtype("text");
        testMessage.setTouser(channel.getOpenId());
        //String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + channel.getOpenId();
        //String urlcustomer = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + byUsername.getOpenId();

        Map<String, Object> customermap = wechatController.getUserInfoByOpenid(byUsername.getOpenId());
        //渠道微信昵称
        Map<String, Object> channelmap = wechatController.getUserInfoByOpenid(channel.getOpenId());
        Object nickname = channelmap.get("nickname");
        String nickName = null;
        if (nickname != null) {
            nickName = nickname.toString();
        } else {
            nickName = "";
        }
        //客户微信昵称
        Object customernickname = customermap.get("nickname");
        String customernickName = null;
        if (customernickname != null) {
            customernickName = customernickname.toString();
        } else {
            customernickName = "";
        }
        Map<String, Object> map = new HashMap<>();
        resources.setCustomerNickname(customernickName);
        resources.setExcel("http://eladmin.asinone.vip/api/saleOrder/Excel?openId=" + customerOpenId + "&invitation=" + invitation + "&id=");
        SaleOrderDTO saleOrderDTO = saleOrderService.create(resources);
        Long id = saleOrderDTO.getId();
        map.put("content", "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。<a href='http://eladmin.asinone.vip/api/saleOrder/Excel?openId=" + customerOpenId + "&id=" + id + "&invitation=" + invitation + "'>点击下载Excel</a>");
        testMessage.setText(map);
        String jsonTestMessage = JSONObject.toJSONString(testMessage);
        User invitations = userRepository.findByUsername(resources.getInvitation());
        if (resources.getAccountOrder() != null && !"".equals(resources.getAccountOrder())) {
            //WechatController.httpClientPost(messageUrl, jsonTestMessage);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(saleOrderDTO.getSaleNumber().substring(9));
            sendModelMessage(channel.getOpenId(), "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", saleOrderDTO.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date) + "\n点击详情可下载跟卖Excel", "请收到该通知后，在此公众号里回复「" + saleOrderDTO.getSaleNumber().substring(7) + "」。", "http://eladmin.asinone.vip/api/saleOrder/Excel?openId=" + customerOpenId + "&id=" + id + "&invitation=" + invitation, "#FF0000");
            //mike
            sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk", "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", saleOrderDTO.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date), "销售:" + invitations.getUsername() + "\n点击详情可下载跟卖Excel", "http://eladmin.asinone.vip/api/saleOrder/Excel?openId=" + customerOpenId + "&id=" + id + "&invitation=" + invitation, "");
            //linda
            sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE", "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", saleOrderDTO.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date), "销售:" + invitations.getUsername() + "\n点击详情可下载跟卖Excel", "http://eladmin.asinone.vip/api/saleOrder/Excel?openId=" + customerOpenId + "&id=" + id + "&invitation=" + invitation, "");
            sendModelMessage(invitations.getOpenId(), "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", saleOrderDTO.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date), "点击详情可下载跟卖Excel", "http://eladmin.asinone.vip/api/saleOrder/Excel?openId=" + customerOpenId + "&id=" + id + "&invitation=" + invitation, "");
            //sendMessage(channel.getOpenId(),"亲爱的"+nickName+"，你有一个新的跟卖需要处理，请查收。<a href='http://47.112.136.123:8000/api/saleOrder/Excel?openId="+customerOpenId+"&id="+id+"&invitation="+invitation+"'>点击下载Excel</a>");
        }
        return new ResponseEntity(saleOrderDTO, HttpStatus.CREATED);
    }


    @Log("上传付款截图")
    @PostMapping(value = "/saleOrder/upload")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_UPLOAD')")
    public ResponseEntity upload(@Validated @RequestBody SaleOrder resources) throws Exception {
        SaleOrderDTO byId = saleOrderService.findById(resources.getId());
        ChannelDTO channel = channelService.findById(byId.getChannelId());
        Map<String, Object> channelmap = wechatController.getUserInfoByOpenid(channel.getOpenId());
        Object nickname = channelmap.get("nickname");
        String nickName = null;
        if (nickname != null) {
            nickName = nickname.toString();
        } else {
            nickName = "";
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String status = "";
        if ("1".equals(byId.getStatus())) {
            status = "2";
        } else {
            status = byId.getStatus();
        }
        saleOrderService.upload(resources.getId(), resources.getAccountImg(), resources.getAccountOrder(), status);
        User byUsername = userRepository.findByUsername(byId.getInvitation());
        if ("1".equals(byId.getStatus())) {
            System.out.println("通知消息");
            sendModelMessage(channel.getOpenId(), "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", byId.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date) + "\n点击详情可下载跟卖Excel", "请收到该通知后，在此公众号里回复「" + byId.getSaleNumber().substring(7) + "」。", byId.getExcel() + byId.getId(), "#FF0000");
            sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk", "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", byId.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date), "销售:" + byId.getInvitation() + "\n点击详情可下载跟卖Excel。", byId.getExcel() + byId.getId(), "");
            sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE", "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", byId.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date), "销售:" + byId.getInvitation() + "\n点击详情可下载跟卖Excel。", byId.getExcel() + byId.getId(), "");
            sendModelMessage(byUsername.getOpenId(), "亲爱的" + nickName + "，你有一个新的跟卖需要处理，请查收。", byId.getSaleNumber(), "赶跟卖", "待处理", sdf.format(date), "点击详情可下载跟卖Excel。", byId.getExcel() + byId.getId(), "");
        }
        //sendMessage(channel.getOpenId(),"亲爱的"+nickName+"，你有一个新的跟卖需要处理，请查收。<a href='"+byId.getExcel()+resources.getId()+"'>点击下载Excel</a>");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Log("修改SaleOrder")
    @PutMapping(value = "/saleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody SaleOrder resources) {
        saleOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("撤销订单")
    @PutMapping(value = "/saleOrder/cancel/{remark}")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','CANCEL_SALEORDER')")
    public ResponseEntity cancel(@RequestBody Long[] ids, @PathVariable String remark) throws Exception {
        saleOrderService.cancel(ids, remark);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("更换渠道")
    @PutMapping(value = "/saleOrder/changeChannel/{channelId}")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','CHANGE_CHANNEL')")
    public ResponseEntity changeChannel(@RequestBody Long[] ids, @PathVariable Long channelId) throws Exception {
        //System.out.println("ssssssss");
        ChannelDTO channel = channelService.findById(channelId);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("US", new BigDecimal("7"));
        map.put("JP", new BigDecimal("0.06"));
        map.put("CA", new BigDecimal("5.5"));
        map.put("UK", new BigDecimal("9"));
        map.put("DE", new BigDecimal("8"));
        map.put("FR", new BigDecimal("8"));
        map.put("ES", new BigDecimal("8"));
        map.put("AU", new BigDecimal("8"));
        map.put("IT", new BigDecimal("8"));
        for (Long id : ids) {
            SaleOrderDTO old = saleOrderService.findById(id);
            SaleOrder newOrder = new SaleOrder();
            newOrder.setProjectName("打跟卖");
            newOrder.setSite(old.getSite());
            newOrder.setAsin(old.getAsin());
            newOrder.setFollowType(old.getFollowType());
            newOrder.setFollowPrice(old.getFollowPrice());
            newOrder.setFollowTime(old.getFollowTime());
            newOrder.setFollowShopUrl(old.getFollowShopUrl());
            newOrder.setFollowShopName(old.getFollowShopName());
            newOrder.setAssuranceTime(old.getAssuranceTime());
            newOrder.setStartTime(old.getStartTime());
            newOrder.setCustomerId(old.getCustomerId());
            newOrder.setChannelId(channelId);
            newOrder.setChannelName(channel.getChannelName());
            newOrder.setAccountImg("");
            if ("0".equals(channel.getProductCost())) {
                newOrder.setRemark("￥" + channel.getPrice());
            } else {
                newOrder.setRemark("￥" + (old.getFollowPrice().multiply(map.get(old.getSite())).add(new BigDecimal(channel.getPrice() + ""))));
            }
            newOrder.setExcel(old.getExcel());
            newOrder.setInvitation(old.getInvitation());
            newOrder.setCustomerNickname(old.getCustomerNickname());
            newOrder.setStatus("1");
            newOrder.setNewOrder("0");
            newOrder.setChannelUserId(channel.getUserId());
            SaleOrderDTO saleOrderDTO = saleOrderService.create(newOrder);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("同意撤销")
    @PutMapping(value = "/saleOrder/agree")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','EXAMINE_SALE','EXAMINE_CHANNEL')")
    public ResponseEntity agree(@RequestBody Long[] ids) throws Exception {
        saleOrderService.agree(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("拒绝撤销")
    @PutMapping(value = "/saleOrder/disagree/{remark}")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','EXAMINE_SALE','EXAMINE_CHANNEL')")
    public ResponseEntity disagree(@RequestBody Long[] ids, @PathVariable String remark) throws Exception {
        saleOrderService.disagree(ids, remark);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("标记赶不走")
    @PutMapping(value = "/saleOrder/dissign/{remark}")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_SIGN')")
    public ResponseEntity dissign(@RequestBody Long[] ids, @PathVariable String remark) throws Exception {
        saleOrderService.dissign(ids, remark);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("标记赶走")
    @PutMapping(value = "/saleOrder/sign/{signdate}")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_SIGN')")
    public ResponseEntity sign(@RequestBody Long[] ids, @PathVariable String signdate) throws Exception {
        for (Long id : ids) {
            SaleOrderDTO resources = saleOrderService.findById(id);
            UserDTO byId = userService.findById(resources.getCustomerId());
            String accessToken = WechatController.token;
            String messageUrl = "";
            if (accessToken != null) {
                messageUrl = content_openid.replace("ACCESS_TOKEN", accessToken);
            }
            TestMessage testMessage = new TestMessage();
            testMessage.setMsgtype("text");
            testMessage.setTouser(byId.getOpenId());
            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + byId.getOpenId();
            Map<String, Object> usermap = wechatController.getUserInfoByOpenid(byId.getOpenId());
            Object nickname = usermap.get("nickname");
            String nickName = null;
            if (nickname != null) {
                nickName = nickname.toString();
            } else {
                nickName = "";
            }
            Map<String, Object> map = new HashMap<>();
            //map.put("content", "亲爱的"+nickName+"，你的asin:"+resources.getAsin()+",跟卖店铺:"+resources.getFollowShopName()+",已经赶走了,请查收哈。如果后续有回跟,在我们网站上点击【有回跟】按钮即可,我们会立即再次处理。");
            testMessage.setText(map);
            String jsonTestMessage = JSONObject.toJSONString(testMessage);
            if (accessToken != null) {
                //WechatController.httpClientPost(messageUrl, jsonTestMessage);
            }
            sendModelMessage(byId.getOpenId(), "亲爱的" + nickName + "，你的asin:" + resources.getAsin() + ",跟卖店铺:" + resources.getFollowShopName() + ",已经赶走了,请查收哈。", resources.getSaleNumber(), "赶跟卖", "已赶走", "", "如果后续有回跟,在我们网站上点击【有回跟】按钮即可,我们会立即再次处理。", "", "");
            //sendMessage(byId.getOpenId(),"亲爱的"+nickName+"，你的asin:"+resources.getAsin()+",跟卖店铺:"+resources.getFollowShopName()+",已经赶走了,请查收哈。如果后续有回跟,在我们网站上点击【有回跟】按钮即可,我们会立即再次处理。");
        }
        saleOrderService.sign(ids, signdate);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("标记处理")
    @PutMapping(value = "/saleOrder/signHandle")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_SIGN')")
    public ResponseEntity signHandle(@RequestBody Long[] ids) throws Exception {
        saleOrderService.signHandle(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("通知回跟")
    @PutMapping(value = "/saleOrder/back")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_BACK')")
    @ResponseBody
    public Object back(@RequestBody Long[] ids) throws Exception {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        Set<Role> roles = byUsername.getRoles();
        boolean back = true;
        for (Role role : roles) {
            if (role.getId() == 2) {
                back = false;
            }
        }
        for (Long id : ids) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
            SaleOrderDTO resources = saleOrderService.findById(id);
            if (resources.getLastBacktime() == null || !simpleDateFormat.format(date).equals(resources.getLastBacktime()) || back) {
                ChannelDTO channel = channelService.findById(resources.getChannelId());
                String accessToken = WechatController.token;
                String messageUrl = "";
                if (accessToken != null) {
                    messageUrl = content_openid.replace("ACCESS_TOKEN", accessToken);
                }
                TestMessage testMessage = new TestMessage();
                testMessage.setMsgtype("text");
                testMessage.setTouser(channel.getOpenId());
                String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + channel.getOpenId();
                Map<String, Object> usermap = httpClientGet(url);
                Object nickname = usermap.get("nickname");
                String nickName = null;
                if (nickname != null) {
                    nickName = nickname.toString();
                } else {
                    nickName = "";
                }
                Map<String, Object> map = new HashMap<>();
                Timestamp startTime = resources.getStartTime();
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");
                String format = sdf1.format(startTime);
                String fileName = format + "-打跟卖-" + resources.getFollowType() + "-" + resources.getAsin() + "-" + resources.getFollowShopName() + "-" + resources.getAssuranceTime() + "-" + resources.getInvitation() + "-" + resources.getCustomerNickname();
                map.put("content", "亲爱的" + nickName + "，【" + fileName + "】订单有回跟,请及时处理！");
                testMessage.setText(map);
                String jsonTestMessage = JSONObject.toJSONString(testMessage);
                if (accessToken != null) {
                    //WechatController.httpClientPost(messageUrl, jsonTestMessage);
                }
                //Date date=new Date();
                saleOrderService.followBack(id);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sendModelMessage(channel.getOpenId(), "亲爱的" + nickName + "，【" + fileName + "】订单有回跟,请及时处理！", resources.getSaleNumber(), "跟卖回跟", "待处理", sdf.format(date), "详情请登录官网查看", "", "");
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("msg", "false");
                //System.out.println(map);
                return map;
            }
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除SaleOrder")
    @DeleteMapping(value = "/saleOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        saleOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/saleOrder/Excel")
    @ApiOperation(value = "根据asin生成Excel")
    public void getExcelExport(String openId, Long id, String invitation, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SaleOrderDTO byId = saleOrderService.findById(id);
        Timestamp startTime = byId.getStartTime();
        String accessToken = WechatController.token;
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");
        String format = sdf1.format(startTime);
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId;
        Map<String, Object> map = httpClientGet(url);
        Object nickname = map.get("nickname");
        String nickName = null;
        if (nickname != null) {
            nickName = nickname.toString();
        } else {
            nickName = "";
        }
        String[] files = {"微信昵称", "跟卖类型(FBM/FBA/VC)", "Asin/链接", "站点国家", "跟卖售价", "跟卖时间(晚上/全天)", "跟卖方店铺链接", "跟卖店铺名", "是否品牌备案", "质保时间", "客户下单日期", "订单类型"};
        List<Object[]> data = new ArrayList<>();
        String fileName = null;
        fileName = format + "-打跟卖-" + byId.getFollowType() + "-" + byId.getAsin() + "-" + byId.getFollowShopName() + "-" + byId.getAssuranceTime() + "-" + invitation + "-" + nickName.replace(" ", "");
        Object[] objects = new Object[12];
        objects[0] = nickName;
        objects[1] = byId.getFollowType();
        objects[2] = byId.getAsin();
        objects[3] = byId.getSite();
        objects[4] = byId.getFollowPrice();
        objects[5] = byId.getFollowTime();
        objects[6] = byId.getFollowShopUrl();
        objects[7] = byId.getFollowShopName();
        objects[8] = byId.getIsaplus();
        objects[9] = byId.getAssuranceTime();
        objects[10] = byId.getStartTime();
        objects[11] = "系统自动生成";
        data.add(objects);
        excelExport(fileName, files, data, response, request);
    }

    @GetMapping("/saleOrder/getSalePaymentCode")
    @ApiOperation(value = "生成支付备注")
    @ResponseBody
    public String getSalePaymentCode() {
        int num = 0;
        while (true) {
            StringBuffer requireNum = new StringBuffer("0");
            //String today = DateUtils.parseDateToStr(new Date(), "yyMMdd");
            //requireNum.append(today);
            Map<String, Object> maps = new HashMap<>();
            maps.put("paymentRemark", requireNum.toString());
            String maxSalesOrderNo = saleOrderRepository.getMaxSalesOrderPaymentRemark(maps.get("paymentRemark") + "%");
            if (StringUtils.isBlank(maxSalesOrderNo)) {
                requireNum.append("001");
            } else {
                String lastStr = maxSalesOrderNo.substring(1);
                int lastNum = Integer.parseInt(lastStr) + 1 + num;
                String lastNumStr = "" + lastNum;
                if (lastNumStr.length() < 3) {
                    lastNumStr = "00" + lastNumStr;
                    lastNumStr = lastNumStr.substring(lastNumStr.length() - 3, lastNumStr.length());
                }
                requireNum.append(lastNumStr);
            }
            WaitPayment waitPayment = waitPaymentRepository.getwaitPayment(requireNum.toString());
            System.out.println(requireNum.toString());
            num++;
            if (waitPayment == null) {
                return requireNum.toString();
            }
        }
    }

    @GetMapping("/open")
    public void getOpen() throws Exception {
        System.setProperty("java.awt.headless", "false");
        Robot robot = new Robot();
        robot.delay(5000);
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
        Thread.sleep(3000);
        robot.keyPress(KeyEvent.VK_F10);
        robot.keyRelease(KeyEvent.VK_F10);
    }

    @Log("赶跟卖")
    @PostMapping(value = "/saleOrder/playFollow")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_BACK')")
    @ResponseBody
    public Object playFollow(@RequestBody Long[] ids) throws Exception {
        for (Long id : ids) {
            SaleOrderDTO saleOrderDTO = saleOrderService.findById(id);
            AmzConfigDTO amz = amzConfigService.findById((long) 1);
            System.setProperty("webdriver.chrome.driver",
                    "D:\\file\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            //options.addArguments("--proxy-server=http://127.0.0.1:10808");
            options.setExperimentalOption("prefs", prefs);
            options.addExtensions(new File("D:\\workspace\\zwcj.crx"));
            WebDriver webDriver = new ChromeDriver(options);
            //JavascriptExecutor js = (JavascriptExecutor) webDriver;
            //js.executeScript("window.localStorage.setItem('account','"+amz.getAmzAccount()+"')");
            webDriver.get("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fcart%2Fview.html%3Fref_%3Dnav_ya_signin%26&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&");
            Thread.sleep(3000);
            Cookie cookie = new Cookie("at-main", "Atza|IwEBIAAXx7YlLijYoAexixFIJI_DiisVJuSb8hCiCdByE1oyAqJB3OVFwDRhC6jguEzQuJuxKKucYzx9j3EGbZLgMKBA5hwT53I1nljYTeYGAWb6Z3Zc_xRDAeFUvWD1UZltpj6mC_62Eo-vlw-QwRJTtRrBohoYCVDW8PuytbGDkXYEPKI1r8QQY2pwlWsG7Q3d6yBnivFoTbM2oxjgl13ESHeRpcNQuTJr_2FyvxZ24vQr0Y6Z6Pzcm6ELjq4r8cC1DNHLyLJ-xdL0h110Y_N9R6Z201gKp7XXIR6U0p0_u0AaJAZKCoRRtqbfcrih_IRGzQQYiwCRx5-pPbC1ETutBD-u54wc2BiW2sOKXkuKjyu8PBdkwfrnrGDqVWCj278N48jZKqpQTX-keUT934Jsy3s7KP_jYBC1dXiSknKsIzuSf_-dC_OdyUENdZdOS7OP1Ug");
            Cookie cookie1 = new Cookie("sess-at-main", "l0RehJ2c/aAGwmIE8m/LtHDCCKz5sQOPNnWjMjL4Qro=");
            Cookie cookie2 = new Cookie("session-id", "134-1154123-9714000");
            Cookie cookies = new Cookie("csm-hit", "tb:s-KXM0ZQY7RKJM2777C463|1567062626409&t:1567062626435&adb:adblk_no");
            Cookie cookie3 = new Cookie("session-id-time", "2082787201l");
            Cookie cookie4 = new Cookie("ubid-main", "130-9751460-0157826");
            Cookie cookie5 = new Cookie("x-main", "\"bFrXj0gKFuGv?V9FQaYs0deUeBWnns22VXRXb5PxhrqQJ0QO3zwFGev@Iysu0i3c\"");
            Cookie cookie6 = new Cookie("x-wl-uid", "12RbgIu56cKifnQSySF7U77M+DIpDHm9sr29Gn8/GNNu4Lm6R2DXoJ4MudnxOfYJpwdcK6YzcNNBqtTCUc9AhXAIThoCKnz+o9cexz/2yRcoXY6+x4qW4oIdIsi9xWL6jkr2mFpwY+vo=");
            Cookie cookie7 = new Cookie("session-token", "l4I51wXEroYwQjhcglvTfXLv5HmPp0neUQ5alz2y5zLoOXrlAOufrfnIJCOWQAdpEMYSt3+NdLQGWw7ZC/l0Glt8CKnwIg63ZMUeXSljfXBMxhp56yPZYtfkyRy4797IXqcLVcU6SvyqUBupmpY6ss15SPHSTzWs77TZxcKfUAU8QhahDS8GFufHBjYv+OgpHXwK9a/2clK/tA2Cy2L+vv+0C1fbxd/AmScPJO0KvWnclObOzjHSYAwhP2Ga+2LD67fUN3WExWxg9s7GyNUg5JIL3ac9gLxj");
            Cookie cookie8 = new Cookie("sst-main", "Sst1|PQFdUuo5MGo8-qLnfbKtoafXC6mVWiTzcydy-Xty8i8MtKPxCXlP64_8veqbvRHZLkyFxuwJ2edZCXtRRh5HFcJnSYUpamEXUHymcyQcDYAKXXQes-UvRR1JBSBQvkfa4MtHOrZfwTH-A57TLySx4B0jRSVcgFRCRgzTWyO0yKUD243zq35o_Tl3CSLgCmK1I-7WlNxAKkUNhAimsMaroHw43fSgy5wHDFrGJ_bidSAgw_zq4ySSkQPugwqXTQKnKd01PJG_GyWtjtEaXNXsliDuCx3tyKlz0IJJ9bXMRDuSVrC15pNLUNUzdU8C2yaY6OBCwP9oDM72fm8RzO6Cd8Fu9g");
            webDriver.manage().addCookie(cookie);
            webDriver.manage().addCookie(cookie1);
            webDriver.manage().addCookie(cookie2);
            webDriver.manage().addCookie(cookie3);
            webDriver.manage().addCookie(cookie4);
            webDriver.manage().addCookie(cookie5);
            webDriver.manage().addCookie(cookie6);
            //webDriver.manage().addCookie(cookie7);
            //webDriver.manage().addCookie(cookie8);
            //webDriver.manage().addCookie(cookies);
            Actions action = new Actions(webDriver);

            //action.click(webDriver.findElement(By.id("")));
            //webDriver.findElement(By.id("ap_email")).click();
            webDriver.findElement(By.id("ap_email")).sendKeys(amz.getAmzAccount());
            try {
                webDriver.findElement(By.id("ap_password")).sendKeys(",.,.123...PXW");
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                    webDriver.findElement(By.id("continue")).click();
                } catch (Exception e1) {
                    webDriver.findElement(By.id("continue")).click();
                }
                Thread.sleep(3000);
                webDriver.findElement(By.id("ap_password")).sendKeys(",.,.123...PXW");
            }
            Thread.sleep(3000);
            WebElement rememberMe = webDriver.findElement(By.cssSelector("input[name='rememberMe']"));
            rememberMe.click();
            Thread.sleep(3000);
            webDriver.findElement(By.id("signInSubmit")).click();
            Thread.sleep(3000);
            webDriver.navigate().to(getUrl(saleOrderDTO.getSite()) + saleOrderDTO.getAsin());
            Thread.sleep(3000);
            List<WebElement> elements = webDriver.findElements(By.cssSelector("#olpOfferList > div > div > div"));
            System.out.println(elements.size());
            for (WebElement element : elements) {
                try {
                    WebElement element1 = element.findElement(By.cssSelector("div.a-column.a-span2.olpSellerColumn > h3 > span > a"));
                    System.out.println(element1.getText());
                    if (element1.getText().equals(saleOrderDTO.getFollowShopName())) {
                        element.findElement(By.className("a-button-input")).click();
                        Thread.sleep(3000);
                        WebElement proceed = webDriver.findElement(By.id("hlb-ptc-btn-native"));
                        proceed.click();
                        try {
                            webDriver.findElement(By.id("ap_password")).sendKeys(",.,.123...PXW");
                            Thread.sleep(1000);
                            WebElement rememberMes = webDriver.findElement(By.cssSelector("input[name='rememberMe']"));
                            rememberMes.click();
                            Thread.sleep(1000);
                            webDriver.findElement(By.id("signInSubmit")).click();
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {

                }
            }

        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    public static String getUrl(String site) {
        Map<String, String> map = new HashMap<>();
        map.put("US", "https://www.amazon.com/gp/offer-listing/");
        map.put("UK", "https://www.amazon.co.uk/gp/offer-listing/");
        map.put("DE", "https://www.amazon.de/gp/offer-listing/");
        map.put("JP", "https://www.amazon.co.jp/gp/offer-listing/");
        map.put("FR", "https://www.amazon.fr/gp/offer-listing/");
        map.put("ES", "https://www.amazon.es/gp/offer-listing/");
        map.put("IT", "https://www.amazon.it/gp/offer-listing/");
        map.put("AU", "https://www.amazon.com.au/gp/offer-listing/");
        map.put("CA", "https://www.amazon.ca/gp/offer-listing/");
        map.put("MX", "https://www.amazon.com.mx/gp/offer-listing/");
        map.put("IN", "https://www.amazon.in/gp/offer-listing/");
        return map.get(site);
    }

    @Log("标记已付款")
    @PutMapping(value = "/saleOrder/signPayment")
    @PreAuthorize("hasAnyRole('ADMIN','SALEORDER_ALL','SALEORDER_SIGNPAYMENT')")
    public ResponseEntity signPayment(@RequestBody Long[] ids) {
        saleOrderService.signPayment(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}