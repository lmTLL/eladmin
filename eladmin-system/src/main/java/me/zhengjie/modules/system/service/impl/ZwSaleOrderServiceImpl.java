package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.ZwPostingEffect;
import me.zhengjie.modules.system.domain.ZwSaleOrder;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.repository.ZwSaleOrderRepository;
import me.zhengjie.modules.system.rest.WechatController;
import me.zhengjie.modules.system.service.ZwChannelService;
import me.zhengjie.modules.system.service.ZwPostingEffectService;
import me.zhengjie.modules.system.service.ZwSaleOrderService;
import me.zhengjie.modules.system.service.dto.ZwChannelDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ZwSaleOrderMapper;
import me.zhengjie.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private ZwPostingEffectService zwPostingEffectService;

    @Override
    public Object queryAll(ZwSaleOrderQueryCriteria criteria, Pageable pageable){
        Page<ZwSaleOrder> page = zwSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(zwSaleOrderMapper::toDto));
    }

    @Override
    public Object queryAll(ZwSaleOrderQueryCriteria criteria){
        return zwSaleOrderMapper.toDto(zwSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public ZwSaleOrderDTO findById(Long id) {
        Optional<ZwSaleOrder> zwSaleOrder = zwSaleOrderRepository.findById(id);
        ValidationUtil.isNull(zwSaleOrder,"ZwSaleOrder","id",id);
        return zwSaleOrderMapper.toDto(zwSaleOrder.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZwSaleOrderDTO create(ZwSaleOrder resources){
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        resources.setCustomerId(customer.getId());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String substring = sss.substring(11, 13);
        String hour = resources.getStartDate().substring(11, 13);
        String day = resources.getStartDate().substring(8, 10);
        int i = Integer.parseInt(hour);
        int i1 = Integer.parseInt(day);
        String format = simpleDateFormat.format(new Date());
        String substring = format.substring(8, 10);
        int i2 = Integer.parseInt(substring);
        if (i>4&&i1>=i2){
            try {
                Date parse = simpleDateFormat.parse(resources.getStartDate());
                Long time=parse.getTime()+(1000*60*60*24);
                Date date=new Date(time);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                resources.setEstimatedTime(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            Date date=new Date();
            resources.setEstimatedTime(simpleDateFormat.format(date).substring(0, 10));
        }
        Map<String, Object> userInfoByOpenid = null;
        try {
            userInfoByOpenid = wechatController.getUserInfoByOpenid(customer.getOpenId());
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
        resources.setCustomerNickname(nickName);
        resources.setFinancePayment("0");
        resources.setInvitation(customer.getInvitation());
        resources.setSubmitTime(new Timestamp(new Date().getTime()));
        ZwChannelDTO byId = zwChannelService.findById(1L);
        resources.setZwChannelId(byId.getId());
        resources.setZwChannelName(byId.getZwChannelName());
        resources.setZwChannelUserId(byId.getUserId());
        resources.setNewOrder("0");
        if (!"".equals(resources.getAccountOrder())){
            //初始化时间状态
            resources.setAccountTime(new Timestamp(new Date().getTime()));
            resources.setStatus("1");
        }else {
            resources.setStatus("0");
        }
        resources.setZwSaleNumber(getSalesOrderNo());
        /*ErpSalesOrder erpSalesOrder=new ErpSalesOrder();
        erpSalesOrder.setPaymentId();
        erpSalesOrder.setWechatId();
        erpSalesOrder.setWechatName();
        erpSalesOrder.setBuyerName();
        erpSalesOrder.setProjectId();
        erpSalesOrder.setStation();
        erpSalesOrder.setQuantity();
        erpSalesOrder.setUnitPrice();
        erpSalesOrder.setTotalPrice();
        erpSalesOrder.setActualAmount();
        erpSalesOrder.setCostUnitPrice();
        erpSalesOrder.setDealTime();
        erpSalesOrder.setIssueOrder();
        erpSalesOrder.setPaymentStatus();
        erpSalesOrder.setRemark();
        erpSalesOrder.setDataStatus();
        erpSalesOrder.setAuditStatus();
        erpSalesOrder.setRejectReason();
        erpSalesOrder.setAlipaySum();
        erpSalesOrder.setAsinInfo();
        erpSalesOrder.setCreateUser();
        erpSalesOrder.setUpdateUser();
        erpSalesOrder.setShopName();*/
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
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String substring = sss.substring(11, 13);
        String hour = sa.getStartDate().substring(11, 13);
        int i = Integer.parseInt(hour);
        if (i>4){
            try {
                Date parse = simpleDateFormat.parse(sa.getStartDate());
                Long time=parse.getTime()+(1000*60*60*24);
                Date date=new Date(time);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                sa.setEstimatedTime(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            sa.setEstimatedTime(sa.getStartDate().substring(0, 10));
        }
        zwSaleOrderRepository.update(sa.getId(),sa.getSite(),sa.getLink(),sa.getProductName(),sa.getDealSite(),sa.getDealPrice(),sa.getOriginalPrice(),sa.getCode(),sa.getCodeWork(),sa.getDiscount(),sa.getStartDate(),sa.getEndDate(),sa.getEstimatedTime());
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
        String maxSalesOrderNo = zwSaleOrderRepository.getMaxSalesOrderNo(maps.get("salesOrderNo")+"%");
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
    public void upload(Long id, String accountImg, String accountOrder,String status) throws Exception {
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        zwSaleOrderRepository.upload(id, accountImg, accountOrder,timestamp,status);
    }

    @Override
    public void feedback(Long id, String[] effectImgs, String effect) {
        ZwPostingEffect zwPostingEffect=new ZwPostingEffect();
        zwPostingEffect.setZwSaleId(id);
        zwPostingEffect.setPostingEffect(effect);
        zwPostingEffect.setSubmitTime(new Timestamp(new Date().getTime()));
        zwPostingEffectService.create(zwPostingEffect);
        ZwSaleOrderDTO byId = findById(id);
        String postingImg ="";
        if (byId!=null){
            postingImg=byId.getPostingImg();
        }
        for (String effectImg : effectImgs) {
            postingImg=postingImg+","+effectImg;
        }
        zwSaleOrderRepository.feedback(id,postingImg,effect,"2");
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
            zwSaleOrderRepository.revoke(id,"3");
        }
    }

    @Override
    public void updateChannelRemark(ZwSaleOrder resources) {
        zwSaleOrderRepository.updateChannelRemark(resources.getId(),resources.getChannelRemark());
    }

    @Override
    public void signPayment(Long[] ids) {
        for (Long id : ids) {
            zwSaleOrderRepository.signPayment(id,"1");
        }
    }
}