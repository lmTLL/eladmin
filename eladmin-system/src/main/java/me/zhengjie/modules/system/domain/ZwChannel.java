package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-09-06
 */
@Entity
@Data
@Table(name = "zw_channel")
public class ZwChannel implements Serializable {

    // 站外渠道id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 渠道名
    @Column(name = "zw_channel_name")
    private String zwChannelName;

    // openID
    @Column(name = "open_id", nullable = false)
    private String openId;

    // 状态
    @Column(name = "enabled")
    private String enabled;

    // 渠道说明
    @Column(name = "remark")
    private String remark;

    // 对应的用户id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}