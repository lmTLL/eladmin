package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-08-27
 */
@Entity
@Data
@Table(name = "amz_config")
public class AmzConfig implements Serializable {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 亚马逊账号
    @Column(name = "amz_account")
    private String amzAccount;

    // 画布随机数
    @Column(name = "num", nullable = false)
    private Long num;

    // ip地址
    @Column(name = "ip")
    private String ip;
    //使用状态
    @Column(name = "status")
    private String status;
}