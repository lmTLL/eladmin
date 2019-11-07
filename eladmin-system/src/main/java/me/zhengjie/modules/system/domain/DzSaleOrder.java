package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-11-01
 */
@Entity
@Data
@Table(name = "dz_sale_order")
public class DzSaleOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 订单编号
    @Column(name = "dz_sale_number", nullable = false)
    private String dzSaleNumber;

    // 昵称
    @Column(name = "nickname")
    private String nickname;

    // 站点
    @Column(name = "site")
    private String site;

    // asin
    @Column(name = "asin")
    private String asin;

    // review链接
    @Column(name = "review_url")
    private String reviewUrl;

    // 点赞数量
    @Column(name = "dz_num")
    private Integer dzNum;

    // 现有数量
    @Column(name = "now_num")
    private Integer nowNum;

    // 下单时间
    @Column(name = "start_date")
    private Timestamp startDate;

    // 付款时间
    @Column(name = "update_date")
    private Timestamp updateDate;

    // 客户id
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    // 支付截图
    @Column(name = "account_img")
    private String accountImg;

    // 支付单号
    @Column(name = "account_order")
    private String accountOrder;

    // 状态
    @Column(name = "status")
    private String status;

    // 所属销售
    @Column(name = "invitation")
    private String invitation;
}