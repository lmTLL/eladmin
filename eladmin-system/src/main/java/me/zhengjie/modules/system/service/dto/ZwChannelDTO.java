package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author groot
 * @date 2019-09-06
 */
@Data
public class ZwChannelDTO implements Serializable {

    // 站外渠道id
    private Long id;

    // 渠道名
    private String zwChannelName;

    // openID
    private String openId;

    // 状态
    private String enabled;

    // 渠道说明
    private String remark;

    // 对应的用户id
    private Long userId;
}