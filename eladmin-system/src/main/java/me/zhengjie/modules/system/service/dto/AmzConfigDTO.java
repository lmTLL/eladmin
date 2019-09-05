package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author groot
* @date 2019-08-27
*/
@Data
public class AmzConfigDTO implements Serializable {

    // ID
    private Long id;

    // 亚马逊账号
    private String amzAccount;

    // 画布随机数
    private Long num;

    // ip地址
    private String ip;
    //使用状态
    private String status;
}