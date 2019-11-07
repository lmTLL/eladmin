package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-10-31
*/
@Data
public class FxSaleOrderDTO implements Serializable {

        private Long id;

            // 订单编号
        private String fxSaleNumber;

            // 昵称
        private String nickName;

            // 站点
        private String site;

            // 老SKU
        private String oldSku;

            // 老FNSKU
        private String oldFnsku;

            // 老Asin
        private String oldAsin;

            // 新Asin
        private String newAsin;

            // 下单时间
        private Timestamp startDate;

            // 成功时间
        private Timestamp updateDate;

            // 状态
        private String status;

        private Long customerId;

            // 所属销售
        private String invitation;

        private Long channelUserId;

            // ERP单号
        private String erpSaleNumber;

            // 是否保留评论
        private String isReviews;
}