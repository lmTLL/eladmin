package me.zhengjie.modules.system.domain;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author groot
* @date 2019-07-09
*/
@Entity
@Data
@Table(name="sale_order")
public class SaleOrder implements Serializable {

    // 序号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 服务项目
    @Column(name = "project_name")
    private String projectName;


    // 站点
    @Column(name = "site")
    private String site;

    // asin
    @Column(name = "asin",nullable = false)
    private String asin;

    // 跟卖类型
    @Column(name = "follow_type")
    private String followType;

    // 跟卖售价
    @Column(name = "follow_price")
    private BigDecimal followPrice;

    // 跟卖时间
    @Column(name = "follow_time")
    private String followTime;

    // 跟卖店铺链接
    @Column(name = "follow_shop_url")
    private String followShopUrl;

    // 跟卖店铺名称
    @Column(name = "follow_shop_name")
    private String followShopName;

    // 是否品牌备案
    @Column(name = "isaplus")
    private String isaplus;

    // 质保时间
    @Column(name = "assurance_time")
    private String assuranceTime;

    // 提交时间
    @Column(name = "start_time")
    private Timestamp startTime;

    // 支付时间
    @Column(name = "account_time")
    private Timestamp accountTime;

    // 支付单号
    @Column(name = "account_order")
    private String accountOrder;

    // 赶走时间
    @Column(name = "out_time")
    private String outTime;

    // 过保时间
    @Column(name = "overdue_time")
    private Timestamp overdueTime;

    // 客户编号
    @Column(name = "customer_id")
    private Long customerId;

    // 渠道编号
    @Column(name = "channel_id")
    private Long channelId;

    // 付款截图
    @Column(name = "account_img")
    private String accountImg;
    // 站点
    @Column(name = "excel")
    private String excel;

    // 渠道名称
    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "invitation")
    private String invitation;

    @Column(name = "status")
    private String status;


    @Column(name = "customer_nickname")
    private String customerNickname;

    @Column(name = "sale_number")
    private String saleNumber;

    @Column(name = "remark")
    private String remark;

    @Column(name = "new_order")
    private String newOrder;

    //上一次点回跟按钮时间
    @Column(name = "last_backtime")
    private String lastBacktime;

    //
    @Column(name = "channel_user_id")
    private Long channelUserId;

    //
    @Column(name = "payment_remark")
    private String paymentRemark;
}