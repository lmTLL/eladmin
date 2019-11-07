package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.ScpSaleOrder;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.ScpSaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.ScpSaleOrderService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.mapper.ScpSaleOrderMapper;
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
import java.text.SimpleDateFormat;
import java.util.*;

import static me.zhengjie.modules.system.rest.WechatController.httpClientPostParam;

/**
 * @author groot
 * @date 2019-10-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScpSaleOrderServiceImpl implements ScpSaleOrderService {

    @Autowired
    private ScpSaleOrderRepository scpSaleOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private ScpSaleOrderMapper scpSaleOrderMapper;

    @Override
    public Object queryAll(ScpSaleOrderQueryCriteria criteria, Pageable pageable) {
        Page<ScpSaleOrder> page = scpSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(scpSaleOrderMapper::toDto));
    }

    @Override
    public Object queryAll(ScpSaleOrderQueryCriteria criteria) {
        return scpSaleOrderMapper.toDto(scpSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public ScpSaleOrderDTO findById(Long id) {
        Optional<ScpSaleOrder> scpSaleOrder = scpSaleOrderRepository.findById(id);
        ValidationUtil.isNull(scpSaleOrder, "ScpSaleOrder", "id", id);
        return scpSaleOrderMapper.toDto(scpSaleOrder.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScpSaleOrderDTO create(ScpSaleOrder resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());

        resources.setCustomerId(customer.getId());
        resources.setErpSaleNumber("");
        resources.setNickName(customer.getUsername());
        resources.setInvitation(customer.getInvitation());
        resources.setStartDate(new Timestamp(new Date().getTime()));
        resources.setStatus("0");
        resources.setCanKillOrderDate(new Timestamp(resources.getStartDate().getTime() + (long) 1000 * 3600 * 24 * 20));
        resources.setScpSaleNumber(getSalesOrderNo());
        return scpSaleOrderMapper.toDto(scpSaleOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScpSaleOrder resources) {
        Optional<ScpSaleOrder> optionalScpSaleOrder = scpSaleOrderRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalScpSaleOrder, "ScpSaleOrder", "id", resources.getId());

        ScpSaleOrder scpSaleOrder = optionalScpSaleOrder.get();
// 此处需自己修改
        resources.setId(scpSaleOrder.getId());
        scpSaleOrderRepository.save(resources);
    }

    @Override
    public void payment(ErpSalesOrder resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        ScpSaleOrderDTO byId = findById(resources.getIds()[0]);
        UserDTO user=null;
        try {
            user= userService.findById(byId.getCustomerId());
        }catch (Exception e){

        }
        List<NameValuePair> list = new ArrayList<>();
        list.add(new NameValuePair("paymentId", resources.getPaymentId()));
        if (user!=null){
            list.add(new NameValuePair("wechatId", user.getVxId()));
            list.add(new NameValuePair("wechatName", user.getUsername()));
            list.add(new NameValuePair("buyerName", user.getUsername()));
        }else {
            list.add(new NameValuePair("wechatId", "-"));
            list.add(new NameValuePair("wechatName", "-"));
            list.add(new NameValuePair("buyerName", "-"));
        }
        list.add(new NameValuePair("projectId", "84"));
        list.add(new NameValuePair("unitPrice", resources.getUnitPrice()+""));
        list.add(new NameValuePair("costUnitPrice", resources.getCostUnitPrice()+""));
        list.add(new NameValuePair("station", byId.getSite()));
        list.add(new NameValuePair("quantity", resources.getQuantity()+""));
        list.add(new NameValuePair("actualAmount", resources.getActualAmount()+""));
        list.add(new NameValuePair("dealTime", new Date().toString()));
        list.add(new NameValuePair("issueOrder", "0"));
        list.add(new NameValuePair("remark", "在线下单-删差评"));
        list.add(new NameValuePair("dataStatus", "1"));
        list.add(new NameValuePair("auditStatus", "0"));
        list.add(new NameValuePair("asinInfo", byId.getAsin()));
        list.add(new NameValuePair("rejectReason", customer.getInvitation()));
        list.add(new NameValuePair("shopName", "~"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = httpClientPostParam("http://39.98.168.25:8082/salesOrders/erpOrder", list);
                //Map<String, Object> map = httpClientPostParam("http://localhost:8001/salesOrders/erpOrder", list);
                for (Long id : resources.getIds()) {
                    scpSaleOrderRepository.updateErp(id,map.get("saleOrderNo").toString());
                }
            }
        }).start();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        scpSaleOrderRepository.deleteById(id);
    }

    @Override
    public String getSalesOrderNo() {
        StringBuffer requireNum = new StringBuffer("SCP");
        String today = DateUtils.parseDateToStr(new Date(), "yyMMdd");
        requireNum.append(today);
        Map<String, Object> maps = new HashMap<>();
        maps.put("salesOrderNo", requireNum.toString());
        String maxSalesOrderNo = scpSaleOrderRepository.getMaxSalesOrderNo(maps.get("salesOrderNo") + "%");
        if (StringUtils.isBlank(maxSalesOrderNo)) {
            requireNum.append("001");
        } else {
            String lastStr = maxSalesOrderNo.substring(10);
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

}