package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author groot
 * @date 2019-07-05
 */
@Data
public class InvitationcodesDTO implements Serializable {

    // ID
    private Long id;

    // 关联用户
    private String username;

    // 邀请码
    private String invitationCode;

    //微信Id
    private String vxId;
    //有效 0 无效 1
    private String enable;
}