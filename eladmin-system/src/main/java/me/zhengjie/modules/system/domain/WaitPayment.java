package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author groot
 * @date 2019-08-01
 */
@Entity
@Data
@Table(name = "wait_payment")
public class WaitPayment implements Serializable {

    // 需要付款id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 支付备注
    @Column(name = "payment_remarks", nullable = false)
    private String paymentRemarks;

    // 支付金额
    @Column(name = "payment_price", nullable = false)
    private BigDecimal paymentPrice;

    // 支付状态
    @Column(name = "payment_type")
    private String paymentType;

    // 支付单号
    @Column(name = "payment_id")
    private String paymentId;
}