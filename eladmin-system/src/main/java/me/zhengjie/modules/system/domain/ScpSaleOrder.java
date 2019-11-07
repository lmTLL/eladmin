package me.zhengjie.modules.system.domain;

import lombok.Data;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-10-25
 */
@Entity
@Data
@Table(name = "scp_sale_order")
public class ScpSaleOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 订单编号
    @Column(name = "scp_sale_number", nullable = false)
    private String scpSaleNumber;

    // 微信ID
    @Column(name = "file_name")
    private String fileName;

    // 昵称
    @Column(name = "nick_name")
    private String nickName;

    // 站点
    @Column(name = "site")
    private String site;

    // asin
    @Column(name = "asin")
    private String asin;

    // 差评链接
    @Column(name = "cp_url")
    private String cpUrl;

    // 下单时间
    @Column(name = "start_date")
    private Timestamp startDate;

    // 可撤单时间
    @Column(name = "can_kill_order_date")
    private Timestamp canKillOrderDate;

    // 删除时间
    @Column(name = "delete_date")
    private Timestamp deleteDate;

    // 状态
    @Column(name = "status")
    private String status;

    // 所属销售
    @Column(name = "invitation")
    private String invitation;

    // 用户id
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    // 所属渠道
    @Column(name = "channel")
    private String channel;
    //erp单号
    @Column(name = "erp_sale_number")
    private String erpSaleNumber;
}