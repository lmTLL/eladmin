package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.SaleOrder;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.ChannelRepository;
import me.zhengjie.modules.system.repository.SaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.rest.WechatController;
import me.zhengjie.modules.system.service.ChannelService;
import me.zhengjie.modules.system.service.SaleOrderService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.ChannelDTO;
import me.zhengjie.modules.system.service.dto.SaleOrderDTO;
import me.zhengjie.modules.system.service.dto.SaleOrderQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.mapper.SaleOrderMapper;
import me.zhengjie.utils.*;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.zhengjie.modules.system.rest.WechatController.*;

/**
* @author groot
* @date 2019-07-09
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SaleOrderServiceImpl implements SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private SaleOrderMapper saleOrderMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WechatController wechatController;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;
    @Override
    public Object queryAll(SaleOrderQueryCriteria criteria, Pageable pageable){
        Page<SaleOrder> page = saleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(saleOrderMapper::toDto));
    }

    @Override
    public Object queryAll(SaleOrderQueryCriteria criteria){
        return saleOrderMapper.toDto(saleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public SaleOrderDTO findById(Long id) {
        Optional<SaleOrder> saleOrder = saleOrderRepository.findById(id);
        ValidationUtil.isNull(saleOrder,"SaleOrder","id",id);
        return saleOrderMapper.toDto(saleOrder.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleOrderDTO create(SaleOrder resources) {
        //Date date=new Date();
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        resources.setFinancePayment("0");
        resources.setSaleNumber(getSalesOrderNo());
        List<NameValuePair> list=new ArrayList<>();
        list.add(new NameValuePair("paymentId",resources.getAccountOrder()));
        list.add(new NameValuePair("wechatId",customer.getVxId()));
        list.add(new NameValuePair("wechatName",customer.getUsername()));
        list.add(new NameValuePair("buyerName",customer.getUsername()));
        list.add(new NameValuePair("projectId","12"));
        list.add(new NameValuePair("station",resources.getSite()));
        list.add(new NameValuePair("quantity","1"));
        list.add(new NameValuePair("actualAmount",new BigDecimal(resources.getRemark().substring(1))+""));
        //list.add(new NameValuePair("dealTime",new Date().toString()));
        list.add(new NameValuePair("issueOrder","0"));
        list.add(new NameValuePair("remark","在线下单"));
        list.add(new NameValuePair("dataStatus","1"));
        list.add(new NameValuePair("auditStatus","0"));
        list.add(new NameValuePair("asinInfo",resources.getAsin()));
        list.add(new NameValuePair("rejectReason",customer.getInvitation()));
        list.add(new NameValuePair("shopName","~"));
        /*erpSalesOrder.setPaymentId(resources.getAccountOrder());
        erpSalesOrder.setWechatId(customer.getVxId());
        erpSalesOrder.setWechatName(customer.getUsername());
        erpSalesOrder.setBuyerName(customer.getUsername());
        erpSalesOrder.setProjectId(25);
        erpSalesOrder.setStation(resources.getSite());
        erpSalesOrder.setQuantity(1);
        erpSalesOrder.setActualAmount(new BigDecimal(resources.getRemark().substring(1)));
        erpSalesOrder.setDealTime(new Date());
        erpSalesOrder.setIssueOrder(0);
        erpSalesOrder.setPaymentStatus(1);
        erpSalesOrder.setRemark("在线下单");
        erpSalesOrder.setDataStatus(1);
        erpSalesOrder.setAuditStatus(0);
        erpSalesOrder.setAsinInfo(resources.getLink());
        erpSalesOrder.setRejectReason("mark");
        erpSalesOrder.setShopName("~");*/
        System.out.println(resources.getStatus());
        if ("2".equals(resources.getStatus())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpClientPostParam("http://39.98.168.25:8082/salesOrders/erpOrder",list);
                }
            }).start();
        }
        return saleOrderMapper.toDto(saleOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SaleOrder resources) {
//        Optional<SaleOrder> optionalSaleOrder = saleOrderRepository.findById(resources.getId());
//        ValidationUtil.isNull( optionalSaleOrder,"SaleOrder","id",resources.getId());
//
//        SaleOrder saleOrder = optionalSaleOrder.get();
//        // 此处需自己修改
//        resources.setId(saleOrder.getId());
//        saleOrderRepository.save(resources);
          saleOrderRepository.updateInfo(resources.getId(),resources.getSite(),resources.getAsin(),resources.getFollowType(),resources.getFollowPrice(),resources.getFollowTime(),resources.getFollowShopUrl(),resources.getFollowShopName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        saleOrderRepository.deleteById(id);
    }

    @Override
    public void sign(Long[] ids,String signdate) throws ParseException {
        for (Long id : ids) {
            SaleOrderDTO byId = findById(id);
            // Date date=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(signdate);
            Timestamp timestamp2=null;
            if("15天".equals(byId.getAssuranceTime())){
                timestamp2=new Timestamp(date.getTime()+(long)1000*(long)60*(long)60*(long)24*(long)15);
            }
            if("一个月".equals(byId.getAssuranceTime())){
                timestamp2=new Timestamp(date.getTime()+(long)1000*(long)60*(long)60*(long)24*(long)30);
            }
            if("三个月".equals(byId.getAssuranceTime())){
                timestamp2=new Timestamp(date.getTime()+(long)1000*(long)60*(long)60*(long)24*(long)90);
            }
            saleOrderRepository.signSaleOrder(id,signdate,timestamp2);
        }
    }

    @Override
    public void signHandle(Long[] ids) {
        for (Long id : ids) {
            saleOrderRepository.signHandle(id);
        }
    }

    @Override
    public void dissign(Long[] ids,String remark) throws Exception {
        for (Long id : ids) {
            SaleOrderDTO byId = findById(id);
            User invitation = userRepository.findByUsername(byId.getInvitation());
            UserDTO customer = userService.findById(byId.getCustomerId());
            Map<String, Object> customermap =wechatController.getUserInfoByOpenid(customer.getOpenId());
            Object nickname = customermap.get("nickname");
            String nickName=null;
            if (nickname!=null){
                nickName=nickname.toString();
            }else {
                nickName="";
            }
            Date date=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendModelMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation()+"，很抱歉的通知你",byId.getSaleNumber(),"赶跟卖","赶不走",sdf.format(date),"由于【"+remark+"】,"+byId.getChannelName()+"渠道目前赶不走，可以选择换个其他渠道继续操作，或者撤单退款处理。","","");
            //sendMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation()+",很抱歉的通知你,打跟卖订单编号："+byId.getSaleNumber()+",由于【"+remark+"】,"+byId.getChannelName()+"渠道目前赶不走，可以选择换个其他渠道继续操作，或者撤单退款处理。");
            sendModelMessage(customer.getOpenId(),"亲爱的"+nickName+",你的asin："+byId.getAsin(),byId.getSaleNumber(),"赶跟卖","赶不走",sdf.format(date),"由于【"+remark+"】,"+byId.getChannelName()+"渠道目前赶不走，可以选择换个其他渠道继续操作，或者撤单退款处理。","","");
            //sendMessage(customer.getOpenId(),"亲爱的"+nickName+",你的asin："+byId.getAsin()+",由于【"+remark+"】,"+byId.getChannelName()+"渠道目前赶不走，可以选择换个其他渠道继续操作，或者撤单退款处理。");
            saleOrderRepository.cancelOrder(id,remark,"10");
        }
    }
    //撤销订单
    @Override
    public void cancel(Long[] ids, String remark) {
        for (Long id : ids) {
            SaleOrderDTO byId = findById(id);
            User invitation = userRepository.findByUsername(byId.getInvitation());
            System.out.println(invitation.getOpenId());
            try {
                Date date=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sendModelMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation(),byId.getSaleNumber(),"赶跟卖","请求撤单中，待销售审核",sdf.format(date),"客户要求撤单,请在系统内进行审核。","","");
                //sendMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation()+",打跟卖订单编号："+byId.getSaleNumber()+",客户要求撤单,请在系统内进行审核。");
            } catch (Exception e) {
                e.printStackTrace();
            }
            saleOrderRepository.cancelOrder(id,remark,"5");
        }
    }
    //同意撤单
    @Override
    public void agree(Long[] ids) throws Exception {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId()==6||role.getId()==1||role.getId()==4){
                for (Long id : ids) {
                    SaleOrderDTO byId = findById(id);
                    UserDTO customer = userService.findById(byId.getCustomerId());
                    User invitation = userRepository.findByUsername(byId.getInvitation());
                    Map<String, Object> customermap =wechatController.getUserInfoByOpenid(customer.getOpenId());
                    Object nickname = customermap.get("nickname");
                    String nickName=null;
                    if (nickname!=null){
                        nickName=nickname.toString();
                    }else {
                        nickName="";
                    }
                    try {
                        Date date=new Date();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sendModelMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation(),byId.getSaleNumber(),"赶跟卖","撤单成功！",sdf.format(date),"渠道已同意撤单,请知悉。","","");
                        //sendMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation()+",打跟卖订单编号："+byId.getSaleNumber()+",渠道已同意撤单,请知悉。");
                        sendModelMessage(customer.getOpenId(),"亲爱的"+nickName,byId.getSaleNumber(),"赶跟卖","撤单成功！",sdf.format(date),"渠道已同意撤单,请知悉。","","");
                        //sendMessage(customer.getOpenId(),"亲爱的"+nickName+",打跟卖订单编号："+byId.getSaleNumber()+",渠道已同意撤单,请知悉。");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saleOrderRepository.cancelOrder(id,"","9");
                }
            }else {
                for (Long id : ids) {
                    SaleOrderDTO byId = findById(id);
                    ChannelDTO channels = channelService.findById(byId.getChannelId());
                    //System.out.println(channels.getOpenId());
                    User channel1 = userRepository.findByOpenId(channels.getOpenId());
                    System.out.println(channel1.getUsername());
                    Map<String, Object> channelmap =wechatController.getUserInfoByOpenid(channel1.getOpenId());
                    Object nickname = channelmap.get("nickname");
                    String nickName=null;
                    if (nickname!=null){
                        nickName=nickname.toString();
                    }else {
                        nickName="";
                    }
                    try {
                        Timestamp startTime = byId.getStartTime();
                        SimpleDateFormat sdf1=new SimpleDateFormat("MM.dd");
                        String format = sdf1.format(startTime);
                        String fileName=format+"-打跟卖-"+byId.getFollowType()+"-"+byId.getAsin()+"-"+byId.getFollowShopName()+"-"+byId.getAssuranceTime()+"-"+byId.getInvitation()+"-"+byId.getCustomerNickname();
                        //map.put("content", "亲爱的"+nickName+"，【"+fileName+"】订单有回跟,请及时处理！");
                        Date date=new Date();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sendModelMessage(channel1.getOpenId(),"亲爱的"+nickName,byId.getSaleNumber(),"赶跟卖","请求撤单，待渠道审核！",sdf.format(date),"【"+fileName+"】订单客户要求撤单，请及时登陆系统处理，谢谢！","","");
                        //sendMessage(channel1.getOpenId(),"亲爱的"+nickName+",【"+fileName+"】订单客户要求撤单，请及时登陆系统处理，谢谢！");
                        //System.out.println(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saleOrderRepository.cancelOrder(id,"","7");
                }
            }
        }
    }
    //拒绝撤单
    @Override
    public void disagree(Long[] ids, String remark) throws Exception {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId()==6||role.getId()==1||role.getId()==4){
                for (Long id : ids) {
                    SaleOrderDTO byId = findById(id);
                    UserDTO customer = userService.findById(byId.getCustomerId());
                    //User customer = userRepository.getOne(byId.getCustomerId());
                    User invitation = userRepository.findByUsername(byId.getInvitation());
                    Map<String, Object> customermap =wechatController.getUserInfoByOpenid(customer.getOpenId());
                    Object nickname = customermap.get("nickname");
                    String nickName=null;
                    if (nickname!=null){
                        nickName=nickname.toString();
                    }else {
                        nickName="";
                    }
                    try {
                        Date date=new Date();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sendModelMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation(),byId.getSaleNumber(),"赶跟卖","渠道拒绝撤单",sdf.format(date),"渠道不同意撤单,原因：【"+remark+"】,请及时登录系统,进行处理,谢谢！","","");
                        //sendMessage(invitation.getOpenId(),"亲爱的"+byId.getInvitation()+",打跟卖订单编号："+byId.getSaleNumber()+",渠道不同意撤单,原因：【"+remark+"】,请及时登录系统,进行处理,谢谢！");
                        sendModelMessage(customer.getOpenId(),"亲爱的"+nickName,byId.getSaleNumber(),"赶跟卖","渠道拒绝撤单",sdf.format(date),"渠道不同意撤单,原因：【"+remark+"】,请及时登录系统,进行处理,谢谢！","","");
                        //sendMessage(customer.getOpenId(),"亲爱的"+nickName+",打跟卖订单编号："+byId.getSaleNumber()+",渠道不同意撤单,原因：【"+remark+"】,请及时登录系统,进行处理,谢谢！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saleOrderRepository.cancelOrder(id,remark,"8");
                }
            }else {
                for (Long id : ids) {
                    SaleOrderDTO byId = findById(id);
                    UserDTO one = userService.findById(byId.getCustomerId());
                    // User one = userRepository.getOne(byId.getCustomerId());
                    Map<String, Object> channelmap =wechatController.getUserInfoByOpenid(one.getOpenId());
                    Object nickname = channelmap.get("nickname");
                    String nickName=null;
                    if (nickname!=null){
                        nickName=nickname.toString();
                    }else {
                        nickName="";
                    }
                    System.out.println(remark);
                    Date date=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sendModelMessage(one.getOpenId(),"亲爱的"+nickName,byId.getSaleNumber(),"赶跟卖","客服拒绝撤单",sdf.format(date),"客服不同意撤单,原因：【"+remark+"】请及时联系客服,进行处理。","","");
                    sendMessage(one.getOpenId(),"亲爱的"+nickName+",打跟卖订单编号："+byId.getSaleNumber()+",客服不同意撤单,原因：【"+remark+"】请及时联系客服,进行处理。");
                    saleOrderRepository.cancelOrder(id,remark,"6");
                }
            }
        }
    }

    @Override
    public void upload(Long id, String accountImg, String accountOrder,String status) throws Exception {
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        SaleOrderDTO resources = findById(id);
        List<NameValuePair> list=new ArrayList<>();
        UserDTO customer = userService.findById(resources.getCustomerId());
        list.add(new NameValuePair("paymentId",accountOrder));
        list.add(new NameValuePair("wechatId",customer.getVxId()));
        list.add(new NameValuePair("wechatName",customer.getUsername()));
        list.add(new NameValuePair("buyerName",customer.getUsername()));
        list.add(new NameValuePair("projectId","12"));
        list.add(new NameValuePair("station",resources.getSite()));
        list.add(new NameValuePair("quantity","1"));
        list.add(new NameValuePair("actualAmount",new BigDecimal(resources.getRemark().substring(1))+""));
        list.add(new NameValuePair("dealTime",new Date().toString()));
        list.add(new NameValuePair("issueOrder","0"));
        list.add(new NameValuePair("remark","在线下单"));
        list.add(new NameValuePair("dataStatus","1"));
        list.add(new NameValuePair("auditStatus","0"));
        list.add(new NameValuePair("asinInfo",resources.getAsin()));
        list.add(new NameValuePair("rejectReason",customer.getInvitation()));
        list.add(new NameValuePair("shopName","~"));
        /*erpSalesOrder.setPaymentId(resources.getAccountOrder());
        erpSalesOrder.setWechatId(customer.getVxId());
        erpSalesOrder.setWechatName(customer.getUsername());
        erpSalesOrder.setBuyerName(customer.getUsername());
        erpSalesOrder.setProjectId(25);
        erpSalesOrder.setStation(resources.getSite());
        erpSalesOrder.setQuantity(1);
        erpSalesOrder.setActualAmount(new BigDecimal(resources.getRemark().substring(1)));
        erpSalesOrder.setDealTime(new Date());
        erpSalesOrder.setIssueOrder(0);
        erpSalesOrder.setPaymentStatus(1);
        erpSalesOrder.setRemark("在线下单");
        erpSalesOrder.setDataStatus(1);
        erpSalesOrder.setAuditStatus(0);
        erpSalesOrder.setAsinInfo(resources.getLink());
        erpSalesOrder.setRejectReason("mark");
        erpSalesOrder.setShopName("~");*/
        if ("1".equals(resources.getStatus())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpClientPostParam("http://39.98.168.25:8082/salesOrders/erpOrder",list);
                }
            }).start();
        }
        saleOrderRepository.upload(id, accountImg, accountOrder,timestamp,status);
    }

    @Override
    public void followBack(Long id) throws Exception {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMdd");
        saleOrderRepository.lastBack(id,simpleDateFormat.format(date));
    }

    @Override
    public String getSalesOrderNo() {
        StringBuffer requireNum = new StringBuffer("DGM");
        String today = DateUtils.parseDateToStr(new Date(), "yyMMdd");
        requireNum.append(today);
        Map<String, Object> maps = new HashMap<>();
        maps.put("salesOrderNo", requireNum.toString());
        String maxSalesOrderNo = saleOrderRepository.getMaxSalesOrderNo(maps.get("salesOrderNo")+"%");
        if (StringUtils.isBlank(maxSalesOrderNo))
        {
            requireNum.append("001");
        }
        else
        {
            String lastStr = maxSalesOrderNo.substring(10);
            int lastNum = Integer.parseInt(lastStr) + 1;
            String lastNumStr = "" + lastNum;
            if (lastNumStr.length() < 3)
            {
                lastNumStr = "00" + lastNumStr;
                lastNumStr = lastNumStr.substring(lastNumStr.length() - 3, lastNumStr.length());
            }
            requireNum.append(lastNumStr);
        }
        return requireNum.toString();
    }
    @Override
    public void signPayment(Long[] ids) {
        for (Long id : ids) {
            saleOrderRepository.signPayment(id,"1");
        }
    }
}