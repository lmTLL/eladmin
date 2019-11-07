package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-10-25
*/
@Data
public class ScpSaleOrderDTO implements Serializable {

        private Long id;

            // 订单编号
        private String scpSaleNumber;

            // 文件名
        private String fileName;

            // 昵称
        private String nickName;

            // 站点
        private String site;

            // asin
        private String asin;

            // 差评链接
        private String cpUrl;

            // 下单时间
        private Timestamp startDate;

            // 可撤单时间
        private Timestamp canKillOrderDate;

    private Timestamp deleteDate;

            // 状态
        private String status;

            // 所属销售
        private String invitation;

        private Long customerId;

    // 所属渠道

    private String channel;
    //erp单号

    private String erpSaleNumber;
}