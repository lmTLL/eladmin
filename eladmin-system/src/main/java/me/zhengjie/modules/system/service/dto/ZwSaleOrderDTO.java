package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author groot
 * @date 2019-09-05
 */
@Data
public class ZwSaleOrderDTO implements Serializable {

    // 站外id
    private Long id;

    // 订单编号
    private String zwSaleNumber;

    // 服务项目
    private String projectName;

    private Long zwChannelId;

    // 渠道
    private String zwChannelName;

    // 站外渠道用户id
    private Long zwChannelUserId;

    // 所属销售
    private String invitation;

    // 客户昵称
    private String customerNickname;

    // 站点
    private String site;

    // asin
    private String link;

    private String productName;
    // Deal站
    private String dealSite;

    // Deal price
    private String dealPrice;

    // Original price
    private String originalPrice;

    // code
    private String code;

    private String codeWork;

    // Discount
    private String discount;

    // 开始时间
    private String startDate;

    //预计发帖时间
    private String estimatedTime;

    // 结束时间
    private String endDate;

    // 发帖效果
    private String postingEffect;

    // 发帖截图
    private String postingImg;

    // 提交时间
    private Timestamp submitTime;

    // 支付时间
    private Timestamp accountTime;

    // 支付单号
    private String accountOrder;
    // 支付截图
    private String accountImg;

    // 订单状态
    private String status;

    // 用户id
    private Long customerId;

    // 备注
    private String remark;
    //new Order
    private String newOrder;
    private String channelRemark;
    private String financePayment;

}