package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.ZwSaleOrder;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.ZwChannelService;
import me.zhengjie.modules.system.service.ZwSaleOrderService;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.dto.ZwChannelDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;
import static me.zhengjie.utils.ExportTextUtil.writeToTxt;


/**
* @author groot
* @date 2019-09-05
*/
@RestController
@RequestMapping("api")
public class ZwSaleOrderController {

    @Autowired
    private ZwSaleOrderService zwSaleOrderService;
    @Autowired
    private ZwChannelService zwChannelService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private WechatController wechatController;

    @Log("查询ZwSaleOrder")
    @GetMapping(value = "/zwSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_SELECT')")
    public ResponseEntity getZwSaleOrders(ZwSaleOrderQueryCriteria criteria, Pageable pageable){
        System.out.println(criteria);
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        criteria.setCustomerId(byUsername.getId());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId()==1||role.getId()==7||role.getId()==4){
                criteria.setCustomerId(null);
            }
            if (role.getId()==8){
                String openId = byUsername.getOpenId();
                Long id = byUsername.getId();
                criteria.setCustomerId(null);
                criteria.setStatus("1");
                criteria.setZwChannelUserId(id);
                //List<Channel> channelByOpenId = channelRepository.findChannelByOpenId(openId);
                //criteria.setChannelName(channelByOpenId.get(0).getChannelName());
            }
            if (role.getId()==5){
                criteria.setCustomerId(null);
                String invitation = byUsername.getUsername();
                criteria.setInvitation(invitation);
            }
        }
        return new ResponseEntity(zwSaleOrderService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增ZwSaleOrder")
    @PostMapping(value = "/zwSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ZwSaleOrder resources){
        ZwSaleOrderDTO zwSaleOrderDTO = zwSaleOrderService.create(resources);
        ZwChannelDTO byId = zwChannelService.findById(zwSaleOrderDTO.getZwChannelId());
        Map<String, Object> userInfoByOpenid = null;
        try {
            userInfoByOpenid = wechatController.getUserInfoByOpenid(byId.getOpenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object nickname = userInfoByOpenid.get("nickname");
        String nickName=null;
        if (nickname!=null){
            nickName=nickname.toString();
        }else {
            nickName="";
        }
        //sendModelMessage(channel.getOpenId(),"亲爱的"+nickName+"，你有一个新的跟卖需要处理，请查收。",saleOrderDTO.getSaleNumber(),"赶跟卖","待处理",sdf.format(date)+"\n点击详情可下载跟卖Excel","请收到该通知后，在此公众号里回复「"+saleOrderDTO.getSaleNumber().substring(7)+"」。","http://eladmin.asinone.vip/api/saleOrder/Excel?openId="+customerOpenId+"&id="+id+"&invitation="+invitation,"#FF0000");
        User byUsername = userRepository.findByUsername(zwSaleOrderDTO.getInvitation());
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
           if (!"".equals(zwSaleOrderDTO.getAccountOrder())){
               sendModelMessage(byId.getOpenId(),"亲爱的"+nickName+"，你有一个新的站外订单需要处理，请查收。",zwSaleOrderDTO.getZwSaleNumber(),"站外","待处理",sdf.format(date),"","","");
               sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk","亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",zwSaleOrderDTO.getZwSaleNumber(),"站外","待处理",sdf.format(date)+"\n客户昵称:"+zwSaleOrderDTO.getCustomerNickname(),"销售:"+zwSaleOrderDTO.getInvitation(),"","");
               sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE","亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",zwSaleOrderDTO.getZwSaleNumber(),"站外","待处理",sdf.format(date)+"\n客户昵称:"+zwSaleOrderDTO.getCustomerNickname(),"销售:"+zwSaleOrderDTO.getInvitation(),"","");
               sendModelMessage(byUsername.getOpenId(),"亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",zwSaleOrderDTO.getZwSaleNumber(),"站外","待处理",sdf.format(date),"客户昵称:"+zwSaleOrderDTO.getCustomerNickname(),"","");
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(zwSaleOrderDTO,HttpStatus.CREATED);
    }

    @Log("修改ZwSaleOrder")
    @PutMapping(value = "/zwSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ZwSaleOrder resources){
        zwSaleOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除ZwSaleOrder")
    @DeleteMapping(value = "/zwSaleOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        zwSaleOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("上传付款截图")
    @PostMapping(value = "/zwSaleOrder/upload")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_UPLOAD')")
    public ResponseEntity upload(@Validated @RequestBody ZwSaleOrder resources) throws Exception {
        ZwSaleOrderDTO byId = zwSaleOrderService.findById(resources.getId());
        ZwChannelDTO zwChannelDTO = zwChannelService.findById(byId.getZwChannelId());
        Map<String, Object> channelmap =wechatController.getUserInfoByOpenid(zwChannelDTO.getOpenId());
        Object nickname = channelmap.get("nickname");
        String nickName=null;
        if (nickname!=null){
            nickName=nickname.toString();
        }else {
            nickName="";
        }
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String status="";
        if ("0".equals(byId.getStatus())){
            status="1";
        }else {
            status=byId.getStatus();
        }
        zwSaleOrderService.upload(resources.getId(),resources.getAccountImg(),resources.getAccountOrder(),status);
        User byUsername = userRepository.findByUsername(byId.getInvitation());
        if ("0".equals(byId.getStatus())){
            System.out.println("通知消息");
            sendModelMessage(zwChannelDTO.getOpenId(),"亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",byId.getZwSaleNumber(),"站外","待处理",sdf.format(date),"","","#FF0000");
            sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk","亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",byId.getZwSaleNumber(),"站外","待处理",sdf.format(date)+"\n客户昵称:"+byId.getCustomerNickname(),"销售:"+byId.getInvitation(),"","");
            sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE","亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",byId.getZwSaleNumber(),"站外","待处理",sdf.format(date)+"\n客户昵称:"+byId.getCustomerNickname(),"销售:"+byId.getInvitation(),"","");
            sendModelMessage(byUsername.getOpenId(),"亲爱的"+nickName+"，你有一个新的站外需要处理，请查收。",byId.getZwSaleNumber(),"站外","待处理",sdf.format(date),"客户昵称:"+byId.getCustomerNickname(),"","");
        }
        //sendMessage(channel.getOpenId(),"亲爱的"+nickName+"，你有一个新的跟卖需要处理，请查收。<a href='"+byId.getExcel()+resources.getId()+"'>点击下载Excel</a>");
        return new ResponseEntity(HttpStatus.CREATED);
    }


    @Log("发帖反馈")
    @PostMapping(value = "/zwSaleOrder/feedback")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_FEEDBACK')")
    public ResponseEntity feedback(@Validated @RequestBody ZwSaleOrder resources){
        System.out.println(resources.getId());
        System.out.println(resources.getPostingEffect());
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        zwSaleOrderService.feedback(resources.getId(),resources.getEffectImgs(),resources.getPostingEffect());
        ZwSaleOrderDTO byId = zwSaleOrderService.findById(resources.getId());
        UserDTO customer = userService.findById(byId.getCustomerId());
        try {
            sendModelMessage(customer.getOpenId(),"亲爱的"+byId.getCustomerNickname()+"，你的站外已反馈，请登录asinone在线下单系统查看。",byId.getZwSaleNumber(),"站外","已反馈",sdf.format(date),"","","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @Log("标记安排")
    @PutMapping(value = "/zwSaleOrder/arrange")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_ARRANGE')")
    public ResponseEntity arrange(@RequestBody Long[] ids){
        zwSaleOrderService.arrange(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "/zwSaleOrder/downloadTxt")
    //@PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_ARRANGE')")
    public void downloadTxt(HttpServletResponse response){
        ZwSaleOrderQueryCriteria criteria=new ZwSaleOrderQueryCriteria();
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        criteria.setEstimatedTime(simpleDateFormat.format(date));
        criteria.setNewOrder("0");
        criteria.setStatus("1");
        List<ZwSaleOrderDTO> list=(List<ZwSaleOrderDTO>) zwSaleOrderService.queryAll(criteria);
        String text="";
        int i=1;
        for (ZwSaleOrderDTO sale : list) {
            text=text+i+".订单号：" +sale.getZwSaleNumber()+
                    "\nWeChat nickname : "+sale.getInvitation()+"-"+sale.getCustomerNickname()+"\n" +
                    "Deal站 : "+sale.getDealSite()+"\n" +
                    "Product name : "+sale.getProductName()+"\n" +
                    "Link : "+sale.getLink()+"\n" +
                    "Deal Price : "+sale.getDealPrice()+"\n" +
                    "Original Price : "+sale.getOriginalPrice()+"\n" +
                    "Code : "+sale.getCode()+"\n" +
                    "Discount : "+sale.getDiscount()+"% OFF\n" +
                    "Start Date : "+sale.getStartDate()+"\n" +
                    "End Date : "+sale.getEndDate()+"\n\n";
            i++;
        }
       // String list = "";
        //String s = JSON.toJSONString(list);
        writeToTxt(response,text,simpleDateFormat.format(date)+"待发帖订单");
    }
    @Log("撤销订单")
    @PutMapping(value = "/zwSaleOrder/revoke")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_REVOKE')")
    public ResponseEntity revoke(@RequestBody Long[] ids){
        zwSaleOrderService.revoke(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("渠道修改备注")
    @PostMapping(value = "/zwSaleOrder/updateChannelRemark")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_CHANNEL_REMARK')")
    public ResponseEntity updateChannelRemark(@Validated @RequestBody ZwSaleOrder resources){
        zwSaleOrderService.updateChannelRemark(resources);
        System.out.println(resources);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Log("标记已付款")
    @PutMapping(value = "/zwSaleOrder/signPayment")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDER_ALL','ZWSALEORDER_SIGNPAYMENT')")
    public ResponseEntity signPayment(@RequestBody Long[] ids){
        zwSaleOrderService.signPayment(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}