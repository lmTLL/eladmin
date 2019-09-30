package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author groot
 * @date 2019-08-01
 */
@Data
public class WaitPaymentDTO implements Serializable {

    // 需要付款id
    private Long id;

    // 支付备注
    private String paymentRemarks;

    // 支付金额
    private BigDecimal paymentPrice;

    // 支付状态
    private String paymentType;
    //支付单号
    private String paymentId;
}