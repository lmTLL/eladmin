package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-07-09
*/
@Data
public class SaleOrderDTO implements Serializable {

    // 序号
    private Long id;

    // 服务项目
    private String projectName;

    // 站点
    private String site;

    // asin
    private String asin;

    // 跟卖类型
    private String followType;

    // 跟卖售价
    private BigDecimal followPrice;

    // 跟卖时间
    private String followTime;

    // 跟卖店铺链接
    private String followShopUrl;

    // 跟卖店铺名称
    private String followShopName;

    // 是否品牌备案
    private String isaplus;

    // 质保时间
    private String assuranceTime;

    // 提交时间
    private Timestamp startTime;

    // 支付时间
    private Timestamp accountTime;

    // 支付单号
    private String accountOrder;

    // 赶走时间
    private String outTime;

    // 过保时间
    private Timestamp overdueTime;

    // 客户编号
    private Long customerId;

    // 渠道编号
    private Long channelId;

    // 付款截图
    private String accountImg;
    //Excl链接
    private String excel;
    //渠道
    private String channelName;
    //邀请者
    private String invitation;
    //订单状态
    private String paymentType;

    //客户昵称
    private String customerNickname;
    //订单编号
    private String saleNumber;
    //订单状态
    private String status;
    //订单备注
    private String remark;
    //新订单
    private String newOrder;
    //上一次点回跟按钮时间
    private String lastBacktime;
    //渠道用户Id
    private Long channelUserId;
    //付款备注
    private String paymentRemark;

    private String financePayment;


}