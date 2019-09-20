package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author groot
* @date 2019-09-05
*/
@Entity
@Data
@Table(name="zw_sale_order")
public class ZwSaleOrder implements Serializable {

    // 站外id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 订单编号
    @Column(name = "zw_sale_number",nullable = false)
    private String zwSaleNumber;

    // 服务项目
    @Column(name = "project_name")
    private String projectName;

    @Column(name = "zw_channel_id")
    private Long zwChannelId;

    // 渠道
    @Column(name = "zw_channel_name")
    private String zwChannelName;

    // 站外渠道用户id
    @Column(name = "zw_channel_user_id")
    private Long zwChannelUserId;

    // 所属销售
    @Column(name = "invitation")
    private String invitation;

    // 客户昵称
    @Column(name = "customer_nickname")
    private String customerNickname;

    // 站点
    @Column(name = "site")
    private String site;

    // asin
    @Column(name = "link")
    private String link;

    // Deal站
    @Column(name = "deal_site")
    private String dealSite;

    // 产品名称
    @Column(name = "product_name")
    private String productName;

    // Deal price
    @Column(name = "deal_price")
    private String dealPrice;

    // Original price
    @Column(name = "original_price")
    private String originalPrice;

    // code
    @Column(name = "code")
    private String code;

    // codeWork
    @Column(name = "code_work")
    private String codeWork;

    // Discount
    @Column(name = "discount")
    private String discount;

    // 开始时间
    @Column(name = "start_date")
    private String startDate;

    // 预计发帖时间
    @Column(name = "estimated_time")
    private String estimatedTime;

    // 结束时间
    @Column(name = "end_date")
    private String endDate;

    // 发帖效果
    @Column(name = "posting_effect")
    private String postingEffect;

    // 发帖截图
    @Column(name = "posting_img")
    private String postingImg;

    // 提交时间
    @Column(name = "submit_time")
    private Timestamp submitTime;

    // 支付时间
    @Column(name = "account_time")
    private Timestamp accountTime;

    // 支付单号
    @Column(name = "account_order")
    private String accountOrder;

    // 支付截图
    @Column(name = "account_img")
    private String accountImg;

    // 订单状态
    @Column(name = "status")
    private String status;

    // 用户id
    @Column(name = "customer_id",nullable = false)
    private Long customerId;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 备注
    @Column(name = "new_order")
    private String newOrder;
    @Transient
    private String[] effectImgs;
    @Column(name = "channel_remark")
    private String channelRemark;

    @Column(name = "finance_payment")
    private String financePayment;
}