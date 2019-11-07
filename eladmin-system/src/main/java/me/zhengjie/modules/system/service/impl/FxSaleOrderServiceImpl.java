package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.FxSaleOrder;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.FxSaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.FxSaleOrderService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.FxSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.FxSaleOrderQueryCriteria;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.mapper.FxSaleOrderMapper;
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

import java.sql.Timestamp;
import java.util.*;

import static me.zhengjie.modules.system.rest.WechatController.httpClientPostParam;

/**
 * @author groot
 * @date 2019-10-31
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FxSaleOrderServiceImpl implements FxSaleOrderService {

    @Autowired
    private FxSaleOrderRepository fxSaleOrderRepository;

    @Autowired
    private FxSaleOrderMapper fxSaleOrderMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public Object queryAll(FxSaleOrderQueryCriteria criteria, Pageable pageable) {
        Page<FxSaleOrder> page = fxSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(fxSaleOrderMapper::toDto));
    }

    @Override
    public Object queryAll(FxSaleOrderQueryCriteria criteria) {
        return fxSaleOrderMapper.toDto(fxSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public FxSaleOrderDTO findById(Long id) {
        Optional<FxSaleOrder> fxSaleOrder = fxSaleOrderRepository.findById(id);
        ValidationUtil.isNull(fxSaleOrder, "FxSaleOrder", "id", id);
        return fxSaleOrderMapper.toDto(fxSaleOrder.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FxSaleOrderDTO create(FxSaleOrder resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());

        resources.setCustomerId(customer.getId());
        resources.setErpSaleNumber("");
        resources.setNickName(customer.getUsername());
        resources.setInvitation(customer.getInvitation());
        resources.setStartDate(new Timestamp(new Date().getTime()));
        resources.setStatus("0");
        //resources.setCanKillOrderDate(new Timestamp(resources.getStartDate().getTime() + (long) 1000 * 3600 * 24 * 20));
        resources.setFxSaleNumber(getSalesOrderNo());
        return fxSaleOrderMapper.toDto(fxSaleOrderRepository.save(resources));
    }

    @Override
    public void payment(ErpSalesOrder resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        FxSaleOrderDTO byId = findById(resources.getIds()[0]);
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
        list.add(new NameValuePair("projectId", "40"));
        list.add(new NameValuePair("unitPrice", resources.getUnitPrice()+""));
        list.add(new NameValuePair("costUnitPrice", resources.getCostUnitPrice()+""));
        list.add(new NameValuePair("station", byId.getSite()));
        list.add(new NameValuePair("quantity", resources.getQuantity()+""));
        list.add(new NameValuePair("actualAmount", resources.getActualAmount()+""));
        list.add(new NameValuePair("dealTime", new Date().toString()));
        list.add(new NameValuePair("issueOrder", "0"));
        list.add(new NameValuePair("remark", "在线下单-翻新"));
        list.add(new NameValuePair("dataStatus", "1"));
        list.add(new NameValuePair("auditStatus", "0"));
        list.add(new NameValuePair("asinInfo", byId.getNewAsin()));
        list.add(new NameValuePair("rejectReason", customer.getInvitation()));
        list.add(new NameValuePair("shopName", "~"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = httpClientPostParam("http://39.98.168.25:8082/salesOrders/erpOrder", list);
                //Map<String, Object> map = httpClientPostParam("http://localhost:8001/salesOrders/erpOrder", list);
                for (Long id : resources.getIds()) {
                    fxSaleOrderRepository.updateErp(id,map.get("saleOrderNo").toString());
                }
            }
        }).start();
    }

    @Override
    public void sign(ErpSalesOrder resources) {
        for (Long id : resources.getIds()) {
            fxSaleOrderRepository.sign(id,resources.getStatus());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FxSaleOrder resources) {
        Optional<FxSaleOrder> optionalFxSaleOrder = fxSaleOrderRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalFxSaleOrder, "FxSaleOrder", "id", resources.getId());

        FxSaleOrder fxSaleOrder = optionalFxSaleOrder.get();
// 此处需自己修改
        resources.setId(fxSaleOrder.getId());
        fxSaleOrderRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fxSaleOrderRepository.deleteById(id);
    }

    public String getSalesOrderNo() {
        StringBuffer requireNum = new StringBuffer("FX");
        String today = DateUtils.parseDateToStr(new Date(), "yyMMdd");
        requireNum.append(today);
        Map<String, Object> maps = new HashMap<>();
        maps.put("salesOrderNo", requireNum.toString());
        String maxSalesOrderNo = fxSaleOrderRepository.getMaxSalesOrderNo(maps.get("salesOrderNo") + "%");
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
}