package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author groot
* @date 2019-10-22
*/
@Data
public class PrizesDTO implements Serializable {

            // id
        private Long id;

            // openID
        private String openId;

            // 所剩次数
        private Long num;

            // 微信昵称
        private String nickname;

            // 微信头像
        private String headimgurl;

            // 礼物名称
        private String prizesName;

            // 中奖时间
        private Timestamp getprizesDate;

            // 微信ID
        private String vxId;
}