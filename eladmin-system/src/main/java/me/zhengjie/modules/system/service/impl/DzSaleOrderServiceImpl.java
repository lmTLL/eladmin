package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.DzSaleOrder;
import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.DzSaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.DzSaleOrderService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.DzSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.DzSaleOrderQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.mapper.DzSaleOrderMapper;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;

/**
 * @author groot
 * @date 2019-11-01
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DzSaleOrderServiceImpl implements DzSaleOrderService {

    @Autowired
    private DzSaleOrderRepository dzSaleOrderRepository;

    @Autowired
    private DzSaleOrderMapper dzSaleOrderMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public Object queryAll(DzSaleOrderQueryCriteria criteria, Pageable pageable) {
        Page<DzSaleOrder> page = dzSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(dzSaleOrderMapper::toDto));
    }

    @Override
    public Object queryAll(DzSaleOrderQueryCriteria criteria) {
        return dzSaleOrderMapper.toDto(dzSaleOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public DzSaleOrderDTO findById(Long id) {
        Optional<DzSaleOrder> dzSaleOrder = dzSaleOrderRepository.findById(id);
        ValidationUtil.isNull(dzSaleOrder, "DzSaleOrder", "id", id);
        return dzSaleOrderMapper.toDto(dzSaleOrder.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DzSaleOrderDTO create(DzSaleOrder resources) {
        resources.setDzSaleNumber(getSalesOrderNo());
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        resources.setCustomerId(customer.getId());
        resources.setNickname(customer.getUsername());
        resources.setInvitation(customer.getInvitation());
        resources.setStartDate(new Timestamp(new Date().getTime()));
        resources.setStatus("0");
        return dzSaleOrderMapper.toDto(dzSaleOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DzSaleOrder resources) {
        Optional<DzSaleOrder> optionalDzSaleOrder = dzSaleOrderRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalDzSaleOrder, "DzSaleOrder", "id", resources.getId());

        DzSaleOrder dzSaleOrder = optionalDzSaleOrder.get();
// 此处需自己修改
        resources.setId(dzSaleOrder.getId());
        dzSaleOrderRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dzSaleOrderRepository.deleteById(id);
    }

    @Override
    public void payment(ErpSalesOrder resources) {
        for (Long id : resources.getIds()) {
            dzSaleOrderRepository.payment(id,resources.getPaymentId(),resources.getRefundImageUrl());
        }
    }

    @Override
    public void sign(ErpSalesOrder resources) {
        for (Long id : resources.getIds()) {
            DzSaleOrderDTO byId = findById(id);
            UserDTO byId1 = userService.findById(byId.getCustomerId());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                sendModelMessage(byId1.getOpenId(), "亲爱的"+byId1.getUsername()+"，你的点赞订单有反馈，请查收。", "", "点赞", "已反馈", sdf.format(date), "请登录系统查看", "", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            dzSaleOrderRepository.sign(id,resources.getStatus());
        }
    }

    public String getSalesOrderNo() {
        StringBuffer requireNum = new StringBuffer("DZ");
        String today = DateUtils.parseDateToStr(new Date(), "yyMMdd");
        requireNum.append(today);
        Map<String, Object> maps = new HashMap<>();
        maps.put("salesOrderNo", requireNum.toString());
        String maxSalesOrderNo = dzSaleOrderRepository.getMaxSalesOrderNo(maps.get("salesOrderNo") + "%");
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