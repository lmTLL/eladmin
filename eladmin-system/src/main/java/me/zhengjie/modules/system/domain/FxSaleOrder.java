package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author groot
* @date 2019-10-31
*/
@Entity
@Data
@Table(name="fx_sale_order")
public class FxSaleOrder implements Serializable {

            @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

            // 订单编号
        @Column(name = "fx_sale_number",nullable = false)
        private String fxSaleNumber;

            // 昵称
        @Column(name = "nick_name")
        private String nickName;

            // 站点
        @Column(name = "site")
        private String site;

            // 老SKU
        @Column(name = "old_sku")
        private String oldSku;

            // 老FNSKU
        @Column(name = "old_fnsku")
        private String oldFnsku;

            // 老Asin
        @Column(name = "old_asin")
        private String oldAsin;

            // 新Asin
        @Column(name = "new_asin")
        private String newAsin;

            // 下单时间
        @Column(name = "start_date")
        private Timestamp startDate;

            // 成功时间
        @Column(name = "update_date")
        private Timestamp updateDate;

            // 状态
        @Column(name = "status")
        private String status;

        @Column(name = "customer_id")
        private Long customerId;

            // 所属销售
        @Column(name = "invitation")
        private String invitation;

        @Column(name = "channel_user_id")
        private Long channelUserId;

            // ERP单号
        @Column(name = "erp_sale_number")
        private String erpSaleNumber;

            // 是否保留评论
        @Column(name = "is_reviews")
        private String isReviews;
}