package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.FxSaleOrder;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.FxSaleOrderService;
import me.zhengjie.modules.system.service.dto.FxSaleOrderQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


/**
 * @author groot
 * @date 2019-10-31
 */
@RestController
@RequestMapping("api")
public class FxSaleOrderController {

    @Autowired
    private FxSaleOrderService fxSaleOrderService;
    @Autowired
    private UserRepository userRepository;
    @Log("查询FxSaleOrder")
    @GetMapping(value = "/fxSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','FXSALEORDER_ALL','FXSALEORDER_SELECT')")
    public ResponseEntity getFxSaleOrders(FxSaleOrderQueryCriteria criteria, Pageable pageable) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());

        criteria.setCustomerId(byUsername.getId());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId() == 1 || role.getId() == 7 || role.getId() == 4) {
                criteria.setCustomerId(null);
            }
            if (role.getId() == 5) {
                criteria.setCustomerId(null);
                String invitation = byUsername.getUsername();
                criteria.setInvitation(invitation);
            }
        }
        return new ResponseEntity(fxSaleOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增FxSaleOrder")
    @PostMapping(value = "/fxSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','FXSALEORDER_ALL','FXSALEORDER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody FxSaleOrder resources) {
        return new ResponseEntity(fxSaleOrderService.create(resources), HttpStatus.CREATED);
    }

    @Log("生成ERP订单")
    @PostMapping(value = "/fxSaleOrder/payment")
    @PreAuthorize("hasAnyRole('ADMIN','FXSALEORDER_ALL','FXSALEORDER_PAYMENT')")
    public ResponseEntity payment(@Validated @RequestBody ErpSalesOrder resources) {
        fxSaleOrderService.payment(resources);
        return new ResponseEntity( HttpStatus.CREATED);
    }

    @Log("订单反馈")
    @PostMapping(value = "/fxSaleOrder/sign")
    @PreAuthorize("hasAnyRole('ADMIN','FXSALEORDER_ALL','FXSALEORDER_SIGN')")
    public ResponseEntity sign(@Validated @RequestBody ErpSalesOrder resources) {
        fxSaleOrderService.sign(resources);
        return new ResponseEntity( HttpStatus.CREATED);
    }

    @Log("修改FxSaleOrder")
    @PutMapping(value = "/fxSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','FXSALEORDER_ALL','FXSALEORDER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody FxSaleOrder resources) {
        fxSaleOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除FxSaleOrder")
    @DeleteMapping(value = "/fxSaleOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FXSALEORDER_ALL','FXSALEORDER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        fxSaleOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}