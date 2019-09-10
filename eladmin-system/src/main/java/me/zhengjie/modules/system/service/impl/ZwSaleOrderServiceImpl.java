package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.ZwSaleOrder;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.repository.ZwSaleOrderRepository;
import me.zhengjie.modules.system.rest.WechatController;
import me.zhengjie.modules.system.service.ZwChannelService;
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
        return zwSaleOrderMapper.toDto(zwSaleOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ZwSaleOrder resources) {
        Optional<ZwSaleOrder> optionalZwSaleOrder = zwSaleOrderRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalZwSaleOrder,"ZwSaleOrder","id",resources.getId());

        ZwSaleOrder zwSaleOrder = optionalZwSaleOrder.get();
        // 此处需自己修改
        resources.setId(zwSaleOrder.getId());
        zwSaleOrderRepository.save(resources);
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
        String postingImg ="";
        for (String effectImg : effectImgs) {
            postingImg=postingImg+","+effectImg;
        }
        zwSaleOrderRepository.feedback(id,postingImg,effect,"2");
    }
}