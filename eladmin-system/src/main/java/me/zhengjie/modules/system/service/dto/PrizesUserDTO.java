package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author groot
* @date 2019-10-23
*/
@Data
public class PrizesUserDTO implements Serializable {

        private Long id;

            // opneID
        private String openId;

            // 昵称
        private String nickname;

            // 头像
        private String headimgurl;

            // 微信ID
        private String vxId;

    // 所剩次数
    private Long num;
}