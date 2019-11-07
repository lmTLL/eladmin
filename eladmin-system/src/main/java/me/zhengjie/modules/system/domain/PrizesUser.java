package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author groot
* @date 2019-10-23
*/
@Entity
@Data
@Table(name="prizes_user")
public class PrizesUser implements Serializable {

            @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

            // opneID
        @Column(name = "open_id",nullable = false)
        private String openId;

            // 昵称
        @Column(name = "nickname")
        private String nickname;

            // 头像
        @Column(name = "headimgurl")
        private String headimgurl;

            // 微信ID
        @Column(name = "vx_id")
        private String vxId;

    // 所剩次数
    @Column(name = "num")
    private Long num;
}