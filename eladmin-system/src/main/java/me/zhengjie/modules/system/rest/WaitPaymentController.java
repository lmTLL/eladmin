package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.WaitPayment;
import me.zhengjie.modules.system.repository.WaitPaymentRepository;
import me.zhengjie.modules.system.service.WaitPaymentService;
import me.zhengjie.modules.system.service.dto.WaitPaymentQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * @author groot
 * @date 2019-08-01
 */
@RestController
@RequestMapping("api")
public class WaitPaymentController {

    @Autowired
    private WaitPaymentService waitPaymentService;
    @Autowired
    private WaitPaymentRepository waitPaymentRepository;

    @Log("查询WaitPayment")
    @GetMapping(value = "/waitPayment")
    @PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_SELECT')")
    public ResponseEntity getWaitPayments(WaitPaymentQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(waitPaymentService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("查询WaitPayment by paymentRemarks")
    @GetMapping(value = "/waitPayment/{paymentRemarks}")
    //@PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_SELECT')")
    public ResponseEntity getpaymentRemarks(@PathVariable String paymentRemarks) {
        WaitPayment waitPayment = waitPaymentRepository.getwaitPayment(paymentRemarks);
        System.out.println(paymentRemarks);
        System.out.println(waitPayment);
        if ("1".equals(waitPayment.getPaymentType()) || "2".equals(waitPayment.getPaymentType())) {
            waitPaymentRepository.deleteById(waitPayment.getId());
        }
        return new ResponseEntity(waitPayment, HttpStatus.OK);
    }

    @Log("查询所有未查询的WaitPayment")
    @GetMapping(value = "/waitPayment/getAll")
    @ResponseBody
    //@PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_SELECT')")
    public Object getAllWaitPayment(HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        //response.getWriter().print(waitPaymentRepository.getAllwaitPayment());
        return waitPaymentRepository.getAllwaitPayment();
        //return waitPaymentRepository.getAllwaitPayment();
    }

    @Log("更新WaitPayment的状态")
    @GetMapping(value = "/waitPayment/callback")
    //@PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_SELECT')")
    public ResponseEntity callback(String paymentRemarks, String paymentId, String paymentType) {
        waitPaymentRepository.updateType(paymentRemarks, paymentId, paymentType);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("更新WaitPayment的状态")
    @GetMapping(value = "/waitPayment/callbackEl/{paymentRemarks}")
    //@PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_SELECT')")
    public ResponseEntity callbackEl(@PathVariable String paymentRemarks) {
        waitPaymentRepository.updateType(paymentRemarks, "000", "2");
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("新增WaitPayment")
    @PostMapping(value = "/waitPayment")
    //@PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody WaitPayment resources) {
        return new ResponseEntity(waitPaymentService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改WaitPayment")
    @PutMapping(value = "/waitPayment")
    @PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody WaitPayment resources) {
        waitPaymentService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除WaitPayment")
    @DeleteMapping(value = "/waitPayment/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','WAITPAYMENT_ALL','WAITPAYMENT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        waitPaymentService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}