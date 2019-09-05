package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author groot
* @date 2019-07-09
*/
@Data
public class ChannelDTO implements Serializable {

    // 渠道id
    private Long id;

    // 渠道名称
    private String channelName;

    // 跟卖类型
    private String followType;

    // 质保时间
    private String assuranceTime;

    // 价格
    private Integer price;

    // 产品费(0:否 1:是)
    private String productCost;

    // 状态：1启用、0禁用
    private String enabled;

    // 渠道openid
    private String openId;

    //渠道备注
    private String remark;
    // 渠道userId
    private Long userId;
}