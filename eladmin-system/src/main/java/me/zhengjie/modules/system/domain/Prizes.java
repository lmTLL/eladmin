package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author groot
* @date 2019-10-22
*/
@Entity
@Data
@Table(name="prizes")
public class Prizes implements Serializable {

            // id
            @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

            // openID
        @Column(name = "open_id",nullable = false)
        private String openId;

            // 所剩次数
        @Column(name = "num")
        private Long num;

            // 微信昵称
        @Column(name = "nickname")
        private String nickname;

            // 微信头像
        @Column(name = "headimgurl")
        private String headimgurl;

            // 礼物名称
        @Column(name = "prizes_name")
        private String prizesName;

            // 中奖时间
        @Column(name = "getprizes_date")
        private Timestamp getprizesDate;

            // 微信ID
        @Column(name = "vx_id")
        private String vxId;
}