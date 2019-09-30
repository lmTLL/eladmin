package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-07-05
 */
@Entity
@Data
@Table(name = "invitationcodes")
public class Invitationcodes implements Serializable {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 关联用户
    @Column(name = "username", nullable = false)
    private String username;

    // 邀请码
    @Column(name = "invitation_code", nullable = false)
    private String invitationCode;

    // 邀请码
    @Column(name = "vx_id", nullable = false)
    private String vxId;

    //邀请码  有效 0 无效 1
    @Column(name = "enable")
    private String enable;
}