package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-11-01
*/
@Data
public class DzSaleOrderDTO implements Serializable {

        private Long id;

            // 订单编号
        private String dzSaleNumber;

            // 昵称
        private String nickname;

            // 站点
        private String site;

            // asin
        private String asin;

            // review链接
        private String reviewUrl;

            // 点赞数量
        private Integer dzNum;

            // 现有数量
        private Integer nowNum;

            // 下单时间
        private Timestamp startDate;

            // 付款时间
        private Timestamp updateDate;

            // 客户id
        private Long customerId;

            // 支付截图
        private String accountImg;

            // 支付单号
        private String accountOrder;

            // 状态
        private String status;

            // 所属销售
        private String invitation;
}