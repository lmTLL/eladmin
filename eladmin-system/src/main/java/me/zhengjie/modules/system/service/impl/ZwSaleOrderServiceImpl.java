package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.ZwPostingEffect;
import me.zhengjie.modules.system.domain.ZwSaleOrder;
import me.zhengjie.modules.system.domain.ZwSaleOrderUpdate;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.repository.ZwSaleOrderRepository;
import me.zhengjie.modules.system.rest.WechatController;
import me.zhengjie.modules.system.service.*;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.dto.ZwChannelDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ZwSaleOrderMapper;
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

import static me.zhengjie.modules.system.rest.WechatController.httpClientPostParam;
import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;

/**
 * @author groot
 * @date 2019-09-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ZwSaleOrderServiceImpl implements ZwSaleOrderService {

    @Autowired
    private ZwSaleOrderRepository zwSaleOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ZwChannelService zwChannelService;
    @Autowired
    private ZwSaleOrderMapper zwSaleOrderMapper;
    @Autowired
    private WechatController wechatController;
    @Autowired
    private UserService userService;
    @Autowired
    private ZwSaleOrderUpdateService zwSaleOrderUpdateService;
    @Autowired
    private ZwPostingEffectService zwPostingEffectService;

    @Override
    public Object queryAll(ZwSaleOrderQueryCriteria criteria, Pageable pageable) {
        Page<ZwSaleOrder> page = zwSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(zwSaleOrderMapper::toDto));
    }

    @Override
    public Object queryAll(ZwSaleOrderQueryCriteria criteria) {
        return zwSaleOrderMapper.toDto(zwSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public ZwSaleOrderDTO findById(Long id) {
        Optional<ZwSaleOrder> zwSaleOrder = zwSaleOrderRepository.findById(id);
        ValidationUtil.isNull(zwSaleOrder, "ZwSaleOrder", "id", id);
        return zwSaleOrderMapper.toDto(zwSaleOrder.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZwSaleOrderDTO create(ZwSaleOrder resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        resources.setCustomerId(customer.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String substring = sss.substring(11, 13);
        String hour = resources.getStartDate().substring(11, 13);
        String day = resources.getStartDate().substring(8, 10);
        String month = resources.getStartDate().substring(5, 7);
        int i = Integer.parseInt(hour);
        int i1 = Integer.parseInt(day);
        int i3 = Integer.parseInt(month);
        String format = simpleDateFormat.format(new Date());
        String substring = format.substring(8, 10);
        String substring2 = format.substring(5, 7);
        int i2 = Integer.parseInt(substring);
        int i4 = Integer.parseInt(substring2);
        if (i1 >= i2&&i3>=i4) {
            if (i > 3 ){
                try {
                    Date parse = simpleDateFormat.parse(resources.getStartDate());
                    Long time = parse.getTime() + (1000 * 60 * 60 * 24);
                    Date date = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    resources.setEstimatedTime(sdf.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    Date parse = simpleDateFormat.parse(resources.getStartDate());
                    Long time = parse.getTime();
                    Date date = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    resources.setEstimatedTime(sdf.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Date date = new Date();
            resources.setEstimatedTime(simpleDateFormat.format(date).substring(0, 10));
        }
        Map<String, Object> userInfoByOpenid = null;
        try {
            userInfoByOpenid = wechatController.getUserInfoByOpenid(customer.getOpenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object nickname = userInfoByOpenid.get("nickname");
        String nickName = null;
        if (nickname != null) {
            nickName = nickname.toString();
        } else {
            nickName = "";
        }
        resources.setCustomerNickname(nickName);
        resources.setFinancePayment("0");
        resources.setInvitation(customer.getInvitation());
        resources.setSubmitTime(new Timestamp(new Date().getTime()));
        ZwChannelDTO byId = zwChannelService.findById(1L);
        resources.setZwChannelId(byId.getId());
        resources.setZwChannelName(byId.getZwChannelName());
        resources.setZwChannelUserId(byId.getUserId());
        resources.setNewOrder("0");
        if (!"".equals(resources.getAccountOrder())) {
            //初始化时间状态
            resources.setAccountTime(new Timestamp(new Date().getTime()));
            resources.setStatus("1");
        } else {
            resources.setStatus("0");
        }
        resources.setZwSaleNumber(getSalesOrderNo());
        Map<String,String> map=new HashMap();
        map.put("Facebook红人","180");
        map.put("Dealnews","1150");
        map.put("Dealwiki","1050");
        map.put("Slickdeals/L5","350");
        map.put("1Sale/1000 RMB","850");
        map.put("1Sale/1300 RMB","1000");
        map.put("Bensbargain","950");
        map.put("Dealsplus/kinja/Deal2buy/Reddit 合发","350");
        map.put("Retailmenot","280");
        map.put("smartcunks","280");
        map.put("Redflagedeals","280");
        map.put("Dealbunny","500");
        map.put("dealgott.de","580");
        map.put("dealdoctor.de","580");
        map.put("chinagadget","580");
        map.put("mytopdeals","580");
        map.put("monsterdeal","580");
        map.put("schnaeppchenfuchs.com 红人贴","320");
        map.put("serialdealer.fr","500");
        map.put("dealbuzz.fr","500");
        map.put("dealabs 红人","330");
        map.put("28万粉丝Page","360");
        map.put("latestdeals ","900");
        map.put("nolodejesescapar","340");
        map.put("Chollometro","340");
        map.put("gekiyasu-gekiyasu","180");
        map.put("web-price","180");
        map.put("gekiyasu-lab","180");
        map.put("ozbargain 红人帖","320");
        map.put("telegram群","360");
        map.put("mein-deal","680");
        // ErpSalesOrder erpSalesOrder=new ErpSalesOrder();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new NameValuePair("paymentId", resources.getAccountOrder()));
        list.add(new NameValuePair("wechatId", customer.getVxId()));
        list.add(new NameValuePair("wechatName", customer.getUsername()));
        list.add(new NameValuePair("buyerName", customer.getUsername()));
        list.add(new NameValuePair("projectId", "25"));
        list.add(new NameValuePair("station", resources.getSite()));
        list.add(new NameValuePair("quantity", "1"));
        list.add(new NameValuePair("costUnitPrice", map.get(resources.getDealSite())));
        list.add(new NameValuePair("actualAmount", new BigDecimal(resources.getRemark().substring(1)) + ""));
        list.add(new NameValuePair("dealTime", new Date().toString()));
        list.add(new NameValuePair("issueOrder", "0"));
        list.add(new NameValuePair("remark", "在线下单-"+resources.getZwSaleNumber()));
        list.add(new NameValuePair("dataStatus", "1"));
        list.add(new NameValuePair("auditStatus", "0"));
        list.add(new NameValuePair("asinInfo", resources.getLink()));
        list.add(new NameValuePair("rejectReason", resources.getInvitation()));
        list.add(new NameValuePair("shopName", "~"));
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
        if ("1".equals(resources.getStatus())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpClientPostParam("http://39.98.168.25:8082/salesOrders/erpOrder", list);
                }
            }).start();
        }
        return zwSaleOrderMapper.toDto(zwSaleOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ZwSaleOrder sa) {
        /*Optional<ZwSaleOrder> optionalZwSaleOrder = zwSaleOrderRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalZwSaleOrder,"ZwSaleOrder","id",resources.getId());

        ZwSaleOrder zwSaleOrder = optionalZwSaleOrder.get();
        // 此处需自己修改
        resources.setId(zwSaleOrder.getId());*/
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String substring = sss.substring(11, 13);
        String hour = sa.getStartDate().substring(11, 13);
        int i = Integer.parseInt(hour);
        if (i > 4) {
            try {
                Date parse = simpleDateFormat.parse(sa.getStartDate());
                Long time = parse.getTime() + (1000 * 60 * 60 * 24);
                Date date = new Date(time);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sa.setEstimatedTime(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            sa.setEstimatedTime(sa.getStartDate().substring(0, 10));
        }*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String substring = sss.substring(11, 13);
        String hour = sa.getStartDate().substring(11, 13);
        String day = sa.getStartDate().substring(8, 10);
        String month = sa.getStartDate().substring(5, 7);
        int i = Integer.parseInt(hour);
        int i1 = Integer.parseInt(day);
        int i3 = Integer.parseInt(month);
        String format = simpleDateFormat.format(new Date());
        String substring = format.substring(8, 10);
        String substring2 = format.substring(5, 7);
        int i2 = Integer.parseInt(substring);
        int i4 = Integer.parseInt(substring2);
        if (i1 >= i2&&i3>=i4) {
            if (i > 3 ){
                try {
                    Date parse = simpleDateFormat.parse(sa.getStartDate());
                    Long time = parse.getTime() + (1000 * 60 * 60 * 24);
                    Date date = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sa.setEstimatedTime(sdf.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    Date parse = simpleDateFormat.parse(sa.getStartDate());
                    Long time = parse.getTime();
                    Date date = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sa.setEstimatedTime(sdf.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Date date = new Date();
            sa.setEstimatedTime(simpleDateFormat.format(date).substring(0, 10));
        }
        ZwSaleOrderDTO zwSaleOrderDTO = findById(sa.getId());
        UserDTO zwChannel = userService.findById(zwSaleOrderDTO.getZwChannelUserId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        UserDetails userDetails = SecurityUtils.getUserDetails();
        //User byUsername = userRepository.findByUsername(userDetails.getUsername());
        try {
            sendModelMessage(zwChannel.getOpenId(), "亲爱的" + zwChannel.getUsername() + "，该订单有修改，请查收。", zwSaleOrderDTO.getZwSaleNumber(), "站外", "修改记录", sdf.format(date), "修改人：" + userDetails.getUsername(), "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        zwSaleOrderRepository.update(sa.getId(), sa.getSite(), sa.getLink(), sa.getProductName(), sa.getDealSite(), sa.getDealPrice(), sa.getOriginalPrice(), sa.getCode(), sa.getCodeWork(), sa.getDiscount(), sa.getStartDate(), sa.getEndDate(), sa.getEstimatedTime());
        ZwSaleOrderUpdate update = new ZwSaleOrderUpdate();
        update.setZwSaleNumber(zwSaleOrderDTO.getZwSaleNumber());
        update.setBefores(zwSaleOrderDTO.getSite() + "-" + zwSaleOrderDTO.getLink() + "-" + zwSaleOrderDTO.getProductName() + "-" + zwSaleOrderDTO.getDealSite() + "-" + zwSaleOrderDTO.getDealPrice() + "-" + zwSaleOrderDTO.getOriginalPrice() + "-" + zwSaleOrderDTO.getCode() + "-" + zwSaleOrderDTO.getCodeWork() + "-" + zwSaleOrderDTO.getDiscount() + "-" + zwSaleOrderDTO.getStartDate() + "-" + zwSaleOrderDTO.getEndDate() + "-" + zwSaleOrderDTO.getEstimatedTime());
        //ZwSaleOrderDTO now = findById(sa.getId());
        update.setNows(sa.getSite() + "-" + sa.getLink() + "-" + sa.getProductName() + "-" + sa.getDealSite() + "-" + sa.getDealPrice() + "-" + sa.getOriginalPrice() + "-" + sa.getCode() + "-" + sa.getCodeWork() + "-" + sa.getDiscount() + "-" + sa.getStartDate() + "-" + sa.getEndDate() + "-" + sa.getEstimatedTime());
        update.setUpdateUser(userDetails.getUsername());
        zwSaleOrderUpdateService.create(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        zwSaleOrderRepository.deleteById(id);
    }

    public String getSalesOrderNo() {
        StringBuffer requireNum = new StringBuffer("ZW");
        String today = DateUtils.parseDateToStr(new Date(), "yyMMdd");
        requireNum.append(today);
        Map<String, Object> maps = new HashMap<>();
        maps.put("salesOrderNo", requireNum.toString());
        String maxSalesOrderNo = zwSaleOrderRepository.getMaxSalesOrderNo(maps.get("salesOrderNo") + "%");
        System.out.println(maxSalesOrderNo);
        if (StringUtils.isBlank(maxSalesOrderNo)) {
            requireNum.append("001");
        } else {
            String lastStr = maxSalesOrderNo.substring(9);
            int lastNum = Integer.parseInt(lastStr) + 1;
            String lastNumStr = "" + lastNum;
            if (lastNumStr.length() < 3) {
                lastNumStr = "00" + lastNumStr;
                lastNumStr = lastNumStr.substring(lastNumStr.length() - 3, lastNumStr.length());
            }
            requireNum.append(lastNumStr);
        }
        return requireNum.toString();
    }

    @Override
    public void upload(Long id, String accountImg, String accountOrder, String status) throws Exception {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        ZwSaleOrderDTO resources = findById(id);
        UserDTO customer = userService.findById(resources.getCustomerId());
        List<NameValuePair> list = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String substring = sss.substring(11, 13);
        String hour = resources.getStartDate().substring(11, 13);
        String day = resources.getStartDate().substring(8, 10);
        String month = resources.getStartDate().substring(5, 7);
        int i = Integer.parseInt(hour);
        int i1 = Integer.parseInt(day);
        int i3 = Integer.parseInt(month);
        String format = simpleDateFormat.format(new Date());
        String substring = format.substring(8, 10);
        String substring2 = format.substring(5, 7);
        int i2 = Integer.parseInt(substring);
        int i4 = Integer.parseInt(substring2);
        if (i1 >= i2&&i3>=i4) {
            if (i > 3 ){
                try {
                    Date parse = simpleDateFormat.parse(resources.getStartDate());
                    Long time = parse.getTime() + (1000 * 60 * 60 * 24);
                    Date date1 = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    resources.setEstimatedTime(sdf.format(date1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    Date parse = simpleDateFormat.parse(resources.getStartDate());
                    Long time = parse.getTime();
                    Date date1 = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    resources.setEstimatedTime(sdf.format(date1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Date dates = new Date();
            resources.setEstimatedTime(simpleDateFormat.format(dates).substring(0, 10));
        }

        Map<String,String> map=new HashMap();
        map.put("Facebook红人","180");
        map.put("Dealnews","1150");
        map.put("Dealwiki","1050");
        map.put("Slickdeals/L5","350");
        map.put("1Sale/1000 RMB","850");
        map.put("1Sale/1300 RMB","1000");
        map.put("Bensbargain","950");
        map.put("Dealsplus/kinja/Deal2buy/Reddit 合发","350");
        map.put("Retailmenot","280");
        map.put("smartcunks","280");
        map.put("Redflagedeals","280");
        map.put("Dealbunny","500");
        map.put("dealgott.de","580");
        map.put("dealdoctor.de","580");
        map.put("chinagadget","580");
        map.put("mytopdeals","580");
        map.put("monsterdeal","580");
        map.put("schnaeppchenfuchs.com 红人贴","320");
        map.put("serialdealer.fr","500");
        map.put("dealbuzz.fr","500");
        map.put("dealabs 红人","330");
        map.put("28万粉丝Page","360");
        map.put("latestdeals ","900");
        map.put("nolodejesescapar","340");
        map.put("Chollometro","340");
        map.put("gekiyasu-gekiyasu","180");
        map.put("web-price","180");
        map.put("gekiyasu-lab","180");
        map.put("ozbargain 红人帖","320");
        map.put("telegram群","360");
        map.put("mein-deal","680");
        list.add(new NameValuePair("paymentId", accountOrder));
        list.add(new NameValuePair("wechatId", customer.getVxId()));
        list.add(new NameValuePair("wechatName", customer.getUsername()));
        list.add(new NameValuePair("buyerName", customer.getUsername()));
        list.add(new NameValuePair("projectId", "25"));
        list.add(new NameValuePair("costUnitPrice", map.get(resources.getDealSite())));
        list.add(new NameValuePair("station", resources.getSite()));
        list.add(new NameValuePair("quantity", "1"));
        list.add(new NameValuePair("actualAmount", new BigDecimal(resources.getRemark().substring(1)) + ""));
        list.add(new NameValuePair("dealTime", new Date().toString()));
        list.add(new NameValuePair("issueOrder", "0"));
        list.add(new NameValuePair("remark", "在线下单-"+resources.getZwSaleNumber()));
        list.add(new NameValuePair("dataStatus", "1"));
        list.add(new NameValuePair("auditStatus", "0"));
        list.add(new NameValuePair("asinInfo", resources.getLink()));
        list.add(new NameValuePair("rejectReason", resources.getInvitation()));
        list.add(new NameValuePair("shopName", "~"));
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
        if ("0".equals(resources.getStatus())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpClientPostParam("http://39.98.168.25:8082/salesOrders/erpOrder", list);
                }
            }).start();
        }
        zwSaleOrderRepository.upload(id, accountImg, accountOrder, timestamp, status,resources.getEstimatedTime());
    }

    @Override
    public void feedback(Long id, String[] effectImgs, String effect) {
        ZwPostingEffect zwPostingEffect = new ZwPostingEffect();
        zwPostingEffect.setZwSaleId(id);
        zwPostingEffect.setPostingEffect(effect);
        zwPostingEffect.setSubmitTime(new Timestamp(new Date().getTime()));
        zwPostingEffectService.create(zwPostingEffect);
        ZwSaleOrderDTO byId = findById(id);
        String postingImg = "";
        if (byId != null) {
            postingImg = byId.getPostingImg();
        }
        for (String effectImg : effectImgs) {
            postingImg = postingImg + "," + effectImg;
        }
        zwSaleOrderRepository.feedback(id, postingImg, effect, "2");
    }

    @Override
    public void arrange(Long[] ids) {
        for (Long id : ids) {
            zwSaleOrderRepository.arrange(id);
        }

    }

    @Override
    public void revoke(Long[] ids) {
        for (Long id : ids) {
            zwSaleOrderRepository.revoke(id, "3");
        }
    }

    @Override
    public void updateChannelRemark(ZwSaleOrder resources) {
        zwSaleOrderRepository.updateChannelRemark(resources.getId(), resources.getChannelRemark());
    }

    @Override
    public void signPayment(Long[] ids) {
        for (Long id : ids) {
            zwSaleOrderRepository.signPayment(id, "1");
        }
    }
}