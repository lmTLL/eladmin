package me.zhengjie.modules.system.domain;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author groot
* @date 2019-07-09
*/
@Entity
@Data
@Table(name="channel")
public class Channel implements Serializable {

    // 渠道id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 渠道名称
    @Column(name = "channel_name")
    private String channelName;

    // 跟卖类型
    @Column(name = "follow_type")
    private String followType;

    // 质保时间
    @Column(name = "assurance_time")
    private String assuranceTime;

    // 价格
    @Column(name = "price")
    private Integer price;

    // 产品费(0:否 1:是)
    @Column(name = "product_cost")
    private String productCost;

    // 状态：1启用、0禁用
    @Column(name = "enabled")
    private String enabled;

    // 渠道openid
    @Column(name = "open_id")
    private String openId;

    // 渠道备注
    @Column(name = "remark")
    private String remark;

    // 渠道userId
    @Column(name = "user_id")
    private Long userId;
}